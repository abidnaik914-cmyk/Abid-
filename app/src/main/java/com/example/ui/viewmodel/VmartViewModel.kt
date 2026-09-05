package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.VmartDatabase
import com.example.data.model.AnalyticsSummary
import com.example.data.model.CartItemWithProduct
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.repository.VmartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    CATEGORIES,
    ANALYTICS,
    ORDERS,
    CART,
    WISHLIST,
    PRODUCT_DETAIL
}

enum class SortOption(val displayName: String) {
    POPULAR("Popularity"),
    PRICE_LOW_HIGH("Price: Low to High"),
    PRICE_HIGH_LOW("Price: High to Low"),
    RATING("Customer Rating"),
    DISCOUNT("Biggest Discount")
}

data class VmartUiState(
    val selectedScreen: Screen = Screen.HOME,
    val previousScreen: Screen = Screen.HOME,
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val selectedSort: SortOption = SortOption.POPULAR,
    val under199Only: Boolean = false,
    val minRatingFilter: Float = 0f,
    val selectedProduct: ProductEntity? = null,
    val appliedCoupon: String? = null,
    val couponDiscount: Int = 0,
    val showCheckoutDialog: Boolean = false,
    val lastPlacedOrder: OrderEntity? = null,
    val snackbarMessage: String? = null
)

class VmartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VmartRepository

    init {
        val database = VmartDatabase.getDatabase(application)
        repository = VmartRepository(database.vmartDao())
        viewModelScope.launch {
            repository.ensureDataSeeded()
        }
    }

    private val _uiState = MutableStateFlow(VmartUiState())
    val uiState: StateFlow<VmartUiState> = _uiState.asStateFlow()

    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistIds: StateFlow<Set<Int>> = repository.wishlistItems
        .combine(MutableStateFlow(Unit)) { items, _ ->
            items.map { it.productId }.toSet()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val wishlistProducts: StateFlow<List<ProductEntity>> = combine(
        allProducts,
        wishlistIds
    ) { products, ids ->
        products.filter { ids.contains(it.id) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cart items mapped with their corresponding product entities
    val cartItemsWithProducts: StateFlow<List<CartItemWithProduct>> = combine(
        repository.cartItems,
        repository.allProducts
    ) { cartItems, products ->
        val productMap = products.associateBy { it.id }
        cartItems.mapNotNull { cartItem ->
            val product = productMap[cartItem.productId]
            if (product != null) {
                CartItemWithProduct(cartItem = cartItem, product = product)
            } else null
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered & Sorted products for Home/Search
    val displayedProducts: StateFlow<List<ProductEntity>> = combine(
        allProducts,
        _uiState
    ) { products, state ->
        var list = products

        // Filter by category
        if (state.selectedCategory != "All") {
            list = list.filter { it.category.equals(state.selectedCategory, ignoreCase = true) }
        }

        // Filter by under ₹199 store
        if (state.under199Only) {
            list = list.filter { it.price <= 199 }
        }

        // Filter by min rating
        if (state.minRatingFilter > 0f) {
            list = list.filter { it.rating >= state.minRatingFilter }
        }

        // Filter by search query
        if (state.searchQuery.isNotBlank()) {
            val q = state.searchQuery.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                it.category.lowercase().contains(q) ||
                it.subcategory.lowercase().contains(q) ||
                it.supplierName.lowercase().contains(q) ||
                it.description.lowercase().contains(q)
            }
        }

        // Apply sorting
        when (state.selectedSort) {
            SortOption.POPULAR -> list.sortedByDescending { it.unitsSold }
            SortOption.PRICE_LOW_HIGH -> list.sortedBy { it.price }
            SortOption.PRICE_HIGH_LOW -> list.sortedByDescending { it.price }
            SortOption.RATING -> list.sortedByDescending { it.rating }
            SortOption.DISCOUNT -> list.sortedByDescending { it.discountPercent }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Reactive Analytics summary computed from live products & orders in Room
    val analyticsSummary: StateFlow<AnalyticsSummary> = combine(
        allProducts,
        allOrders
    ) { products, orders ->
        repository.calculateAnalytics(products, orders)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        repository.calculateAnalytics(emptyList(), emptyList())
    )

    // Navigation and screen selection
    fun navigateTo(screen: Screen) {
        _uiState.value = _uiState.value.copy(
            previousScreen = _uiState.value.selectedScreen,
            selectedScreen = screen
        )
    }

    fun navigateBack() {
        val target = if (_uiState.value.selectedScreen == Screen.PRODUCT_DETAIL) {
            _uiState.value.previousScreen
        } else {
            Screen.HOME
        }
        _uiState.value = _uiState.value.copy(selectedScreen = target)
    }

    fun selectProduct(product: ProductEntity) {
        _uiState.value = _uiState.value.copy(
            selectedProduct = product,
            previousScreen = _uiState.value.selectedScreen,
            selectedScreen = Screen.PRODUCT_DETAIL
        )
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun setSearchQuery(query: String) = onSearchQueryChanged(query)

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(searchQuery = "")
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            selectedScreen = if (_uiState.value.selectedScreen == Screen.CATEGORIES) Screen.HOME else _uiState.value.selectedScreen
        )
    }

    fun selectSort(sortOption: SortOption) {
        _uiState.value = _uiState.value.copy(selectedSort = sortOption)
    }

    fun setSortOption(sortOption: SortOption) = selectSort(sortOption)

    fun toggleUnder199() {
        _uiState.value = _uiState.value.copy(under199Only = !_uiState.value.under199Only)
    }

    fun setRatingFilter(rating: Float) {
        val nextRating = if (_uiState.value.minRatingFilter == rating) 0f else rating
        _uiState.value = _uiState.value.copy(minRatingFilter = nextRating)
    }

    fun setMinRatingFilter(rating: Float) = setRatingFilter(rating)

    fun resetFilters() {
        _uiState.value = _uiState.value.copy(
            selectedCategory = "All",
            selectedSort = SortOption.POPULAR,
            under199Only = false,
            minRatingFilter = 0f,
            searchQuery = ""
        )
    }

    // Cart actions
    fun addToCart(
        product: ProductEntity,
        size: String = "Free Size",
        quantity: Int = 1,
        resellerMargin: Int = 0
    ) {
        viewModelScope.launch {
            repository.addToCart(product.id, size, quantity, resellerMargin)
            showSnackbar("Added to Cart! (Size: $size)")
        }
    }

    fun buyNow(
        product: ProductEntity,
        size: String = "Free Size",
        resellerMargin: Int = 0
    ) {
        viewModelScope.launch {
            repository.addToCart(product.id, size, 1, resellerMargin)
            _uiState.value = _uiState.value.copy(
                selectedScreen = Screen.CART,
                showCheckoutDialog = true
            )
        }
    }

    fun updateCartQuantity(cartId: Int, quantity: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(cartId, quantity)
        }
    }

    fun updateResellerMargin(cartId: Int, margin: Int) {
        viewModelScope.launch {
            repository.updateResellerMargin(cartId, margin)
        }
    }

    fun removeFromCart(cartId: Int) {
        viewModelScope.launch {
            repository.removeFromCart(cartId)
            showSnackbar("Item removed from Cart")
        }
    }

    fun applyCoupon(code: String): Boolean {
        val clean = code.trim().uppercase()
        return when (clean) {
            "VMART100" -> {
                _uiState.value = _uiState.value.copy(appliedCoupon = clean, couponDiscount = 100)
                showSnackbar("Coupon VMART100 applied! Saved ₹100")
                true
            }
            "MAHASALE" -> {
                _uiState.value = _uiState.value.copy(appliedCoupon = clean, couponDiscount = 150)
                showSnackbar("Maha Sale code applied! Saved ₹150")
                true
            }
            "SAVE50" -> {
                _uiState.value = _uiState.value.copy(appliedCoupon = clean, couponDiscount = 50)
                showSnackbar("Saved ₹50 on this order!")
                true
            }
            else -> {
                showSnackbar("Invalid coupon code. Try VMART100 or MAHASALE")
                false
            }
        }
    }

    fun removeCoupon() {
        _uiState.value = _uiState.value.copy(appliedCoupon = null, couponDiscount = 0)
    }

    // Wishlist actions
    fun toggleWishlist(product: ProductEntity) {
        viewModelScope.launch {
            val isWish = wishlistIds.value.contains(product.id)
            repository.toggleWishlist(product.id, isWish)
            if (!isWish) {
                showSnackbar("Saved to Wishlist")
            } else {
                showSnackbar("Removed from Wishlist")
            }
        }
    }

    // Checkout & Order Placement
    fun openCheckout() {
        _uiState.value = _uiState.value.copy(showCheckoutDialog = true)
    }

    fun dismissCheckout() {
        _uiState.value = _uiState.value.copy(showCheckoutDialog = false)
    }

    fun placeOrder(
        customerName: String,
        customerPhone: String,
        pin: String,
        city: String,
        state: String,
        address: String,
        paymentMethod: String
    ) {
        val currentCart = cartItemsWithProducts.value
        if (currentCart.isEmpty()) return

        val itemsSummary = currentCart.joinToString(", ") { "${it.product.title.take(30)}... (Qty: ${it.cartItem.quantity})" }
        val rawTotal = currentCart.sumOf { it.itemTotal }
        val discount = _uiState.value.couponDiscount
        val finalAmount = (rawTotal - discount).coerceAtLeast(0)
        val resellerProfit = currentCart.sumOf { it.cartItem.resellerMargin * it.cartItem.quantity }

        val newOrderId = "VM-" + (10000..99999).random()

        val order = OrderEntity(
            orderId = newOrderId,
            itemsSummary = itemsSummary,
            itemCount = currentCart.sumOf { it.cartItem.quantity },
            totalAmount = finalAmount,
            discountAmount = discount,
            deliveryFee = 0,
            paymentMethod = paymentMethod,
            customerName = customerName,
            customerPhone = customerPhone,
            addressPin = pin,
            addressCity = city,
            addressState = state,
            addressFull = address,
            orderTimestamp = System.currentTimeMillis(),
            currentStatus = "PLACED",
            deliveryDateEst = "Delivery within 3-4 days",
            resellerProfitTotal = resellerProfit
        )

        viewModelScope.launch {
            repository.placeOrder(order)
            _uiState.value = _uiState.value.copy(
                showCheckoutDialog = false,
                lastPlacedOrder = order,
                appliedCoupon = null,
                couponDiscount = 0,
                selectedScreen = Screen.ORDERS
            )
        }
    }

    fun dismissOrderCelebration() {
        _uiState.value = _uiState.value.copy(lastPlacedOrder = null)
    }

    fun showSnackbar(message: String) {
        _uiState.value = _uiState.value.copy(snackbarMessage = message)
    }

    fun clearSnackbar() {
        _uiState.value = _uiState.value.copy(snackbarMessage = null)
    }
}
