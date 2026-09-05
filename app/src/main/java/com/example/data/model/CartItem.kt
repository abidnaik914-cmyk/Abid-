package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productId: Int,
    val selectedSize: String = "Free Size",
    val quantity: Int = 1,
    val resellerMargin: Int = 0, // In Meesho reselling, seller adds margin to earn
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "wishlist_items")
data class WishlistItemEntity(
    @PrimaryKey
    val productId: Int,
    val addedAt: Long = System.currentTimeMillis()
)

data class CartItemWithProduct(
    val cartItem: CartItemEntity,
    val product: ProductEntity
) {
    val itemTotal: Int
        get() = (product.price + cartItem.resellerMargin) * cartItem.quantity

    val mrpTotal: Int
        get() = product.mrp * cartItem.quantity

    val totalSavings: Int
        get() = (product.mrp - product.price) * cartItem.quantity
}
