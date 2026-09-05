package com.example

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BottomNavBar
import com.example.ui.components.TopSearchBar
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.CheckoutDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderSuccessCelebrationDialog
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.VmartTheme
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VmartViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VmartTheme {
                VmartApp()
            }
        }
    }
}

@Composable
fun VmartApp() {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel: VmartViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    )

    val uiState by viewModel.uiState.collectAsState()
    val displayedProducts by viewModel.displayedProducts.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val wishlistProducts by viewModel.wishlistProducts.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()
    val cartItems by viewModel.cartItemsWithProducts.collectAsState()
    val orders by viewModel.allOrders.collectAsState()
    val analytics by viewModel.analyticsSummary.collectAsState()

    var showCheckoutDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Handle Hardware/System Back Button
    BackHandler(enabled = uiState.selectedScreen != Screen.HOME) {
        when (uiState.selectedScreen) {
            Screen.PRODUCT_DETAIL -> viewModel.navigateTo(Screen.HOME)
            Screen.CART -> viewModel.navigateTo(Screen.HOME)
            Screen.WISHLIST -> viewModel.navigateTo(Screen.HOME)
            Screen.CATEGORIES -> viewModel.navigateTo(Screen.HOME)
            Screen.ANALYTICS -> viewModel.navigateTo(Screen.HOME)
            Screen.ORDERS -> viewModel.navigateTo(Screen.HOME)
            else -> viewModel.navigateTo(Screen.HOME)
        }
    }

    val shouldShowTopBar = uiState.selectedScreen == Screen.HOME || uiState.selectedScreen == Screen.CATEGORIES
    val shouldShowBottomNav = uiState.selectedScreen in listOf(
        Screen.HOME,
        Screen.CATEGORIES,
        Screen.ANALYTICS,
        Screen.ORDERS
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (shouldShowTopBar) {
                TopSearchBar(
                    searchQuery = uiState.searchQuery,
                    onQueryChanged = { viewModel.setSearchQuery(it) },
                    onClearSearch = { viewModel.clearSearch() },
                    wishlistCount = wishlistIds.size,
                    cartCount = cartItems.size,
                    onWishlistClick = { viewModel.navigateTo(Screen.WISHLIST) },
                    onCartClick = { viewModel.navigateTo(Screen.CART) }
                )
            }
        },
        bottomBar = {
            if (shouldShowBottomNav) {
                BottomNavBar(
                    currentScreen = uiState.selectedScreen,
                    orderCount = orders.size,
                    onScreenSelected = { screen ->
                        viewModel.navigateTo(screen)
                    }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)

        when (uiState.selectedScreen) {
            Screen.HOME -> {
                HomeScreen(
                    products = displayedProducts,
                    wishlistIds = wishlistIds,
                    selectedCategory = uiState.selectedCategory,
                    selectedSort = uiState.selectedSort,
                    under199Only = uiState.under199Only,
                    minRatingFilter = uiState.minRatingFilter,
                    searchQuery = uiState.searchQuery,
                    onCategorySelected = { viewModel.selectCategory(it) },
                    onSortSelected = { viewModel.setSortOption(it) },
                    onToggleUnder199 = { viewModel.toggleUnder199() },
                    onSetRatingFilter = { viewModel.setMinRatingFilter(it) },
                    onResetFilters = { viewModel.resetFilters() },
                    onProductClick = { product ->
                        viewModel.selectProduct(product)
                    },
                    onWishlistToggle = { product ->
                        viewModel.toggleWishlist(product)
                        coroutineScope.launch {
                            val msg = if (wishlistIds.contains(product.id)) "Removed from Wishlist" else "Added to Wishlist"
                            snackbarHostState.showSnackbar(msg, duration = SnackbarDuration.Short)
                        }
                    },
                    onApplyCoupon = { coupon ->
                        viewModel.applyCoupon(coupon)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Coupon '$coupon' applied! Enjoy wholesale discount.", duration = SnackbarDuration.Short)
                        }
                    },
                    modifier = screenModifier
                )
            }

            Screen.CATEGORIES -> {
                CategoriesScreen(
                    allProducts = allProducts,
                    onCategorySelected = { category ->
                        viewModel.selectCategory(category)
                        viewModel.navigateTo(Screen.HOME)
                    },
                    modifier = screenModifier
                )
            }

            Screen.PRODUCT_DETAIL -> {
                val currentProduct = uiState.selectedProduct ?: displayedProducts.firstOrNull()
                if (currentProduct != null) {
                    ProductDetailScreen(
                        product = currentProduct,
                        isWishlisted = wishlistIds.contains(currentProduct.id),
                        onBackClick = { viewModel.navigateTo(Screen.HOME) },
                        onWishlistToggle = { product ->
                            viewModel.toggleWishlist(product)
                            coroutineScope.launch {
                                val msg = if (wishlistIds.contains(product.id)) "Removed from Wishlist" else "Added to Wishlist"
                                snackbarHostState.showSnackbar(msg, duration = SnackbarDuration.Short)
                            }
                        },
                        onAddToCart = { product, size, qty, margin ->
                            viewModel.addToCart(product, size, qty, margin)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Added to Cart!", duration = SnackbarDuration.Short)
                            }
                        },
                        onBuyNow = { product, size, margin ->
                            viewModel.addToCart(product, size, 1, margin)
                            viewModel.navigateTo(Screen.CART)
                        },
                        onShareProduct = { shareText ->
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Share with Customers")
                            context.startActivity(shareIntent)
                        },
                        modifier = screenModifier
                    )
                } else {
                    viewModel.navigateTo(Screen.HOME)
                }
            }

            Screen.CART -> {
                CartScreen(
                    cartItems = cartItems,
                    appliedCoupon = uiState.appliedCoupon,
                    couponDiscount = uiState.couponDiscount,
                    onBackClick = { viewModel.navigateTo(Screen.HOME) },
                    onUpdateQuantity = { id, qty -> viewModel.updateCartQuantity(id, qty) },
                    onRemoveItem = { id -> viewModel.removeFromCart(id) },
                    onApplyCoupon = { code ->
                        val success = viewModel.applyCoupon(code)
                        coroutineScope.launch {
                            if (success) {
                                snackbarHostState.showSnackbar("Coupon code '$code' applied successfully!", duration = SnackbarDuration.Short)
                            } else {
                                snackbarHostState.showSnackbar("Invalid coupon code. Try VMART100 or MAHASALE", duration = SnackbarDuration.Short)
                            }
                        }
                        success
                    },
                    onRemoveCoupon = { viewModel.removeCoupon() },
                    onProceedToCheckout = { showCheckoutDialog = true },
                    onExploreProducts = { viewModel.navigateTo(Screen.HOME) },
                    modifier = screenModifier
                )
            }

            Screen.WISHLIST -> {
                WishlistScreen(
                    wishlistProducts = wishlistProducts,
                    onBackClick = { viewModel.navigateTo(Screen.HOME) },
                    onProductClick = { product -> viewModel.selectProduct(product) },
                    onRemoveFromWishlist = { product -> viewModel.toggleWishlist(product) },
                    onMoveToCart = { product ->
                        viewModel.addToCart(product, "Free Size", 1, 0)
                        viewModel.toggleWishlist(product)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Moved to Cart!", duration = SnackbarDuration.Short)
                        }
                    },
                    onExploreShop = { viewModel.navigateTo(Screen.HOME) },
                    modifier = screenModifier
                )
            }

            Screen.ORDERS -> {
                OrdersScreen(
                    orders = orders,
                    onExploreShop = { viewModel.navigateTo(Screen.HOME) },
                    onDownloadInvoice = { order ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Invoice for Order #${order.orderId} saved to downloads!", duration = SnackbarDuration.Short)
                        }
                    },
                    modifier = screenModifier
                )
            }

            Screen.ANALYTICS -> {
                AnalyticsScreen(
                    analytics = analytics,
                    modifier = screenModifier
                )
            }
        }
    }

    // Checkout Modal Dialog
    if (showCheckoutDialog) {
        val totalAmount = (cartItems.sumOf { it.itemTotal } - uiState.couponDiscount).coerceAtLeast(0)
        CheckoutDialog(
            totalAmount = totalAmount,
            onDismiss = { showCheckoutDialog = false },
            onConfirmOrder = { name, phone, pin, city, state, address, paymentMethod ->
                viewModel.placeOrder(
                    customerName = name,
                    customerPhone = phone,
                    pin = pin,
                    city = city,
                    state = state,
                    address = address,
                    paymentMethod = paymentMethod
                )
                showCheckoutDialog = false
                showSuccessDialog = true
            }
        )
    }

    // Order Success Celebration Dialog
    if (showSuccessDialog) {
        val orderId = uiState.lastPlacedOrder?.orderId ?: "VM-98421"
        val totalAmount = (cartItems.sumOf { it.itemTotal } - uiState.couponDiscount).coerceAtLeast(0)
        OrderSuccessCelebrationDialog(
            orderId = orderId,
            totalAmount = totalAmount,
            onTrackOrder = {
                showSuccessDialog = false
                viewModel.navigateTo(Screen.ORDERS)
            },
            onContinueShopping = {
                showSuccessDialog = false
                viewModel.navigateTo(Screen.HOME)
            }
        )
    }
}
