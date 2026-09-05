package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CartItemEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.WishlistItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VmartDao {

    // --- Products ---
    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: Int): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE category = :category ORDER BY unitsSold DESC")
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isTrending = 1 ORDER BY unitsSold DESC")
    fun getTrendingProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE price <= 199 ORDER BY price ASC")
    fun getUnder199Products(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    // --- Cart ---
    @Query("SELECT * FROM cart_items ORDER BY addedAt DESC")
    fun getCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity): Long

    @Query("SELECT * FROM cart_items WHERE productId = :productId AND selectedSize = :size LIMIT 1")
    suspend fun getCartItemByProductAndSize(productId: Int, size: String): CartItemEntity?

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateCartQuantity(id: Int, quantity: Int)

    @Query("UPDATE cart_items SET resellerMargin = :margin WHERE id = :id")
    suspend fun updateResellerMargin(id: Int, margin: Int)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteCartItem(id: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    // --- Wishlist ---
    @Query("SELECT * FROM wishlist_items ORDER BY addedAt DESC")
    fun getWishlistItems(): Flow<List<WishlistItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(item: WishlistItemEntity)

    @Query("DELETE FROM wishlist_items WHERE productId = :productId")
    suspend fun removeFromWishlist(productId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist_items WHERE productId = :productId)")
    fun isWishlisted(productId: Int): Flow<Boolean>

    // --- Orders ---
    @Query("SELECT * FROM orders ORDER BY orderTimestamp DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Query("UPDATE orders SET currentStatus = :status WHERE orderId = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)
}
