package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val orderId: String,
    val itemsSummary: String,
    val itemCount: Int,
    val totalAmount: Int,
    val discountAmount: Int,
    val deliveryFee: Int = 0,
    val paymentMethod: String,
    val customerName: String,
    val customerPhone: String,
    val addressPin: String,
    val addressCity: String,
    val addressState: String,
    val addressFull: String,
    val orderTimestamp: Long = System.currentTimeMillis(),
    val currentStatus: String = "PLACED", // PLACED -> SHIPPED -> OUT_FOR_DELIVERY -> DELIVERED
    val deliveryDateEst: String = "Estimated in 3-4 Days",
    val resellerProfitTotal: Int = 0
)

enum class OrderStatus(val label: String, val description: String) {
    PLACED("Order Placed", "Seller has confirmed your order"),
    SHIPPED("Shipped", "Dispatched from Surat / Delhi Fulfillment Hub"),
    OUT_FOR_DELIVERY("Out for Delivery", "Courier partner is delivering to your address today"),
    DELIVERED("Delivered", "Package safely handed over to you")
}
