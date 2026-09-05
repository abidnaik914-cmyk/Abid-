package com.example.data.repository

import com.example.data.local.InitialData
import com.example.data.local.VmartDao
import com.example.data.model.AnalyticsSummary
import com.example.data.model.CartItemEntity
import com.example.data.model.CategoryShare
import com.example.data.model.DailySalesPoint
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.RegionMetric
import com.example.data.model.TopProductMetric
import com.example.data.model.WishlistItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class VmartRepository(private val vmartDao: VmartDao) {

    val allProducts: Flow<List<ProductEntity>> = vmartDao.getAllProducts()
    val trendingProducts: Flow<List<ProductEntity>> = vmartDao.getTrendingProducts()
    val under199Products: Flow<List<ProductEntity>> = vmartDao.getUnder199Products()
    val cartItems: Flow<List<CartItemEntity>> = vmartDao.getCartItems()
    val wishlistItems: Flow<List<WishlistItemEntity>> = vmartDao.getWishlistItems()
    val allOrders: Flow<List<OrderEntity>> = vmartDao.getAllOrders()

    suspend fun ensureDataSeeded() = withContext(Dispatchers.IO) {
        val currentProducts = vmartDao.getAllProducts().first()
        if (currentProducts.isEmpty()) {
            vmartDao.insertProducts(InitialData.sampleProducts)
            for (order in InitialData.sampleOrders) {
                vmartDao.insertOrder(order)
            }
        }
    }

    fun getProductById(id: Int): Flow<ProductEntity?> = vmartDao.getProductById(id)

    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> {
        return if (category == "All") {
            vmartDao.getAllProducts()
        } else {
            vmartDao.getProductsByCategory(category)
        }
    }

    fun isWishlisted(productId: Int): Flow<Boolean> = vmartDao.isWishlisted(productId)

    suspend fun addToCart(productId: Int, selectedSize: String, quantity: Int = 1, resellerMargin: Int = 0) {
        withContext(Dispatchers.IO) {
            val existing = vmartDao.getCartItemByProductAndSize(productId, selectedSize)
            if (existing != null) {
                vmartDao.updateCartQuantity(existing.id, existing.quantity + quantity)
                if (resellerMargin > 0) {
                    vmartDao.updateResellerMargin(existing.id, resellerMargin)
                }
            } else {
                vmartDao.insertCartItem(
                    CartItemEntity(
                        productId = productId,
                        selectedSize = selectedSize,
                        quantity = quantity,
                        resellerMargin = resellerMargin
                    )
                )
            }
        }
    }

    suspend fun updateCartQuantity(cartId: Int, quantity: Int) {
        withContext(Dispatchers.IO) {
            if (quantity <= 0) {
                vmartDao.deleteCartItem(cartId)
            } else {
                vmartDao.updateCartQuantity(cartId, quantity)
            }
        }
    }

    suspend fun updateResellerMargin(cartId: Int, margin: Int) {
        withContext(Dispatchers.IO) {
            vmartDao.updateResellerMargin(cartId, margin)
        }
    }

    suspend fun removeFromCart(cartId: Int) {
        withContext(Dispatchers.IO) {
            vmartDao.deleteCartItem(cartId)
        }
    }

    suspend fun clearCart() {
        withContext(Dispatchers.IO) {
            vmartDao.clearCart()
        }
    }

    suspend fun toggleWishlist(productId: Int, currentlyWishlisted: Boolean) {
        withContext(Dispatchers.IO) {
            if (currentlyWishlisted) {
                vmartDao.removeFromWishlist(productId)
            } else {
                vmartDao.addToWishlist(WishlistItemEntity(productId = productId))
            }
        }
    }

    suspend fun placeOrder(order: OrderEntity) {
        withContext(Dispatchers.IO) {
            vmartDao.insertOrder(order)
            vmartDao.clearCart()
        }
    }

    fun calculateAnalytics(
        products: List<ProductEntity>,
        orders: List<OrderEntity>
    ): AnalyticsSummary {
        // Base seed metrics from national V-Mart platform operations + local real-time orders
        val baseSeedRevenue = 284500L
        val baseSeedOrders = 1240
        val baseResellerProfit = 46200L

        val ordersRevenue = orders.sumOf { it.totalAmount.toLong() }
        val ordersCount = orders.size
        val ordersProfit = orders.sumOf { it.resellerProfitTotal.toLong() }

        val totalRevenue = baseSeedRevenue + ordersRevenue
        val totalOrders = baseSeedOrders + ordersCount
        val averageOrderValue = if (totalOrders > 0) (totalRevenue / totalOrders).toInt() else 349
        val totalResellerEarnings = baseResellerProfit + ordersProfit

        // Daily trend data points for chart
        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val baseRevenues = listOf(32400, 38900, 42100, 36800, 48500, 56200, 61400)
        val baseOrdersCount = listOf(142, 168, 185, 160, 210, 245, 270)

        // Add recent placed orders to Sunday/Today
        val dailySales = days.mapIndexed { index, day ->
            val addedRev = if (index == 6) ordersRevenue.toInt() else 0
            val addedOrd = if (index == 6) ordersCount else 0
            DailySalesPoint(
                dayLabel = day,
                revenue = baseRevenues[index] + addedRev,
                orderCount = baseOrdersCount[index] + addedOrd
            )
        }

        // Category breakdown
        val categoryGroups = products.groupBy { it.category }
        val categoryColors = listOf(
            0xFF9C155FL, // Magenta
            0xFFFF6F00L, // Saffron
            0xFF00897BL, // Teal
            0xFF1976D2L, // Blue
            0xFF7B1FA2L, // Purple
            0xFFC2185BL, // Pink
            0xFF455A64L  // Slate
        )

        val totalUnitsSold = products.sumOf { it.unitsSold }.coerceAtLeast(1)
        val categoryShares = categoryGroups.entries.mapIndexed { index, entry ->
            val catUnits = entry.value.sumOf { it.unitsSold }
            val catRev = entry.value.sumOf { it.price * it.unitsSold }
            val pct = (catUnits.toFloat() / totalUnitsSold.toFloat()) * 100f
            CategoryShare(
                category = entry.key,
                revenue = catRev,
                percentage = pct,
                colorHex = categoryColors[index % categoryColors.size]
            )
        }.sortedByDescending { it.revenue }

        // Top selling products
        val topProducts = products.sortedByDescending { it.unitsSold }.take(5).map {
            TopProductMetric(
                id = it.id,
                title = it.title,
                unitsSold = it.unitsSold,
                revenue = it.price * it.unitsSold,
                rating = it.rating,
                inStock = it.stockUnits > 20
            )
        }

        // Regional distribution across Indian states
        val regions = listOf(
            RegionMetric("Uttar Pradesh", 28, 384),
            RegionMetric("Maharashtra", 22, 302),
            RegionMetric("Bihar", 16, 220),
            RegionMetric("Karnataka", 14, 192),
            RegionMetric("Gujarat", 11, 151),
            RegionMetric("West Bengal & Others", 9, 124)
        )

        return AnalyticsSummary(
            totalRevenue = totalRevenue,
            totalOrders = totalOrders,
            averageOrderValue = averageOrderValue,
            resellerEarnings = totalResellerEarnings,
            rtoReturnRatePercent = 4.2f,
            customerSatisfaction = 4.4f,
            dailySales = dailySales,
            categoryShares = categoryShares,
            topProducts = topProducts,
            regionalDemographics = regions
        )
    }
}
