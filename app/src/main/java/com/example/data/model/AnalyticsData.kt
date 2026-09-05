package com.example.data.model

data class DailySalesPoint(
    val dayLabel: String,
    val revenue: Int,
    val orderCount: Int
)

data class CategoryShare(
    val category: String,
    val revenue: Int,
    val percentage: Float,
    val colorHex: Long
)

data class TopProductMetric(
    val id: Int,
    val title: String,
    val unitsSold: Int,
    val revenue: Int,
    val rating: Float,
    val inStock: Boolean = true
)

data class RegionMetric(
    val stateName: String,
    val orderSharePercent: Int,
    val orderCount: Int
)

data class AnalyticsSummary(
    val totalRevenue: Long,
    val totalOrders: Int,
    val averageOrderValue: Int,
    val resellerEarnings: Long,
    val rtoReturnRatePercent: Float,
    val customerSatisfaction: Float,
    val dailySales: List<DailySalesPoint>,
    val categoryShares: List<CategoryShare>,
    val topProducts: List<TopProductMetric>,
    val regionalDemographics: List<RegionMetric>
)
