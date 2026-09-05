package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val category: String,
    val subcategory: String,
    val price: Int,
    val mrp: Int,
    val discountPercent: Int,
    val rating: Float,
    val reviewCount: Int,
    val rating5Star: Int = 70,
    val rating4Star: Int = 18,
    val rating3Star: Int = 7,
    val rating2Star: Int = 3,
    val rating1Star: Int = 2,
    val imageUrl: String,
    val supplierName: String,
    val supplierRating: Float = 4.2f,
    val isFreeDelivery: Boolean = true,
    val isCodAvailable: Boolean = true,
    val description: String,
    val material: String,
    val pattern: String,
    val sizes: String = "Free Size", // comma separated
    val returnPolicy: String = "7 Days Easy Returns & Exchange",
    val stockUnits: Int = 150,
    val unitsSold: Int = 1240,
    val isTrending: Boolean = false,
    val isUnder199: Boolean = false
) {
    fun getParsedSizes(): List<String> {
        return sizes.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }
}

data class CategoryItem(
    val id: String,
    val name: String,
    val hindiTag: String = "",
    val iconResName: String = "",
    val productCount: Int = 0
)
