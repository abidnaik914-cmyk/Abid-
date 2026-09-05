package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AnalyticsSummary
import com.example.data.model.DailySalesPoint
import com.example.ui.theme.Amber100
import com.example.ui.theme.Amber50
import com.example.ui.theme.Amber500
import com.example.ui.theme.Amber700
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Indigo100
import com.example.ui.theme.Indigo50
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Indigo700
import com.example.ui.theme.Rose50
import com.example.ui.theme.Rose500
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun AnalyticsScreen(
    analytics: AnalyticsSummary,
    modifier: Modifier = Modifier
) {
    var selectedPeriod by remember { mutableStateOf("This Week") }
    var chartMode by remember { mutableStateOf("Revenue") } // "Revenue" or "Orders"

    val periods = listOf("Today", "This Week", "This Month", "All Time")

    LazyColumn(
        contentPadding = PaddingValues(bottom = 80.dp),
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("analytics_screen")
    ) {
        // Top Header
        item {
            Surface(
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "V-Mart Commerce Analytics",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Emerald500)
                                )
                            }
                            Text(
                                text = "Real-time sales, order trends & reseller margin insights",
                                fontSize = 12.sp,
                                color = Slate500
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Emerald50,
                            border = BorderStroke(1.dp, Emerald100)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = Emerald500,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "+18.4% WoW",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Emerald700
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Time Period Filter row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        periods.forEach { period ->
                            val isSelected = selectedPeriod == period
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) Indigo600 else Slate100,
                                modifier = Modifier
                                    .clickable { selectedPeriod = period }
                                    .testTag("analytics_period_$period")
                            ) {
                                Text(
                                    text = period,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Slate700,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 1: Executive KPI Cards Grid
        item {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                Text(
                    text = "Key Business Metrics",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Row 1: Total GMV & Total Orders
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    KpiMetricCard(
                        title = "Gross Sales (GMV)",
                        value = "₹${formatCurrency(analytics.totalRevenue)}",
                        subValue = "+₹14.2k today",
                        isPositive = true,
                        icon = Icons.Default.CurrencyRupee,
                        iconTint = Indigo600,
                        containerTint = Indigo50,
                        modifier = Modifier.weight(1f)
                    )
                    KpiMetricCard(
                        title = "Total Orders",
                        value = "${analytics.totalOrders}",
                        subValue = "AOV: ₹${analytics.averageOrderValue}",
                        isPositive = true,
                        icon = Icons.Default.LocalMall,
                        iconTint = Slate700,
                        containerTint = Slate100,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Row 2: Reseller Profit & RTO Rate
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    KpiMetricCard(
                        title = "Reseller Margins",
                        value = "₹${formatCurrency(analytics.resellerEarnings)}",
                        subValue = "Meesho Model",
                        isPositive = true,
                        icon = Icons.Default.MonetizationOn,
                        iconTint = Amber700,
                        containerTint = Amber50,
                        modifier = Modifier.weight(1f)
                    )
                    KpiMetricCard(
                        title = "Return / RTO Rate",
                        value = "%.1f%%".format(analytics.rtoReturnRatePercent),
                        subValue = "Industry Low (<5%)",
                        isPositive = true,
                        icon = Icons.Default.CheckCircle,
                        iconTint = Emerald700,
                        containerTint = Emerald50,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Section 2: Interactive Sales & Orders Bar Chart
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Slate200),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .testTag("sales_trend_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = null,
                                tint = Indigo600,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Weekly Sales Trend",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                        }

                        // Mode toggle chips
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            ChartToggleChip(
                                label = "Revenue",
                                isSelected = chartMode == "Revenue",
                                onClick = { chartMode = "Revenue" }
                            )
                            ChartToggleChip(
                                label = "Orders",
                                isSelected = chartMode == "Orders",
                                onClick = { chartMode = "Orders" }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Custom Compose Bar Chart
                    SalesTrendBarChart(
                        data = analytics.dailySales,
                        isRevenueMode = chartMode == "Revenue"
                    )
                }
            }
        }

        // Section 3: Category Revenue Distribution
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Slate200),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .testTag("category_analytics_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PieChart,
                            contentDescription = null,
                            tint = Indigo600,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Category Performance Breakdown",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    analytics.categoryShares.forEach { cat ->
                        Column(modifier = Modifier.padding(vertical = 5.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = cat.category,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Slate800
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "₹${formatCurrency(cat.revenue.toLong())}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Slate900
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "(%.1f%%)".format(cat.percentage),
                                        fontSize = 11.sp,
                                        color = Slate500
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { (cat.percentage / 100f).coerceIn(0f, 1f) },
                                color = Color(cat.colorHex),
                                trackColor = Slate100,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )
                        }
                    }
                }
            }
        }

        // Section 4: Top Performing Products Leaderboard
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Slate200),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .testTag("top_products_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Top Selling Products",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Text(
                            text = "By Units Sold",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    analytics.topProducts.forEachIndexed { index, item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            // Rank Badge
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (index) {
                                            0 -> Amber500
                                            1 -> Slate400
                                            2 -> Color(0xFFCD7F32)
                                            else -> Slate100
                                        }
                                    )
                            ) {
                                Text(
                                    text = "#${index + 1}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (index < 3) Color.White else Slate700
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Slate900,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${item.unitsSold} units sold",
                                        fontSize = 10.sp,
                                        color = Slate500
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "• ${item.rating}★",
                                        fontSize = 10.sp,
                                        color = Emerald500,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "₹${formatCurrency(item.revenue.toLong())}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                        }

                        if (index < analytics.topProducts.size - 1) {
                            HorizontalDivider(color = Slate100)
                        }
                    }
                }
            }
        }

        // Section 5: Regional Demographics (Indian States)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Slate200),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .testTag("regional_analytics_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Indigo600,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Geographical Demand Distribution",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    analytics.regionalDemographics.forEach { region ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = region.stateName,
                                fontSize = 12.sp,
                                color = Slate800,
                                modifier = Modifier.width(130.dp)
                            )
                            LinearProgressIndicator(
                                progress = { region.orderSharePercent / 100f },
                                color = Indigo600,
                                trackColor = Slate100,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "${region.orderSharePercent}% (${region.orderCount})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Slate500,
                                modifier = Modifier.width(64.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KpiMetricCard(
    title: String,
    value: String,
    subValue: String,
    isPositive: Boolean,
    icon: ImageVector,
    iconTint: Color,
    containerTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Slate200),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = Slate500,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(containerTint)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Slate900
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isPositive) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = Emerald500,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
                Text(
                    text = subValue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isPositive) Emerald700 else Slate500
                )
            }
        }
    }
}

@Composable
fun ChartToggleChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Indigo600 else Slate100,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Slate700,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun SalesTrendBarChart(
    data: List<DailySalesPoint>,
    isRevenueMode: Boolean
) {
    val maxValue = if (isRevenueMode) {
        data.maxOfOrNull { it.revenue }?.coerceAtLeast(1) ?: 1
    } else {
        data.maxOfOrNull { it.orderCount }?.coerceAtLeast(1) ?: 1
    }

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(top = 8.dp, bottom = 4.dp)
    ) {
        data.forEach { point ->
            val value = if (isRevenueMode) point.revenue else point.orderCount
            val heightFraction = (value.toFloat() / maxValue.toFloat()).coerceIn(0.12f, 1f)
            val isPeakDay = point.dayLabel == "Sun" || point.dayLabel == "Sat"

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                // Number label on top
                Text(
                    text = if (isRevenueMode) "${value / 1000}k" else "$value",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate500
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Bar
                Box(
                    modifier = Modifier
                        .width(18.dp)
                        .fillMaxSize(heightFraction)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(
                            if (isPeakDay) Indigo600 else Indigo100
                        )
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Day of Week Label
                Text(
                    text = point.dayLabel,
                    fontSize = 10.sp,
                    fontWeight = if (isPeakDay) FontWeight.Bold else FontWeight.Normal,
                    color = if (isPeakDay) Indigo600 else Slate700
                )
            }
        }
    }
}

private fun formatCurrency(amount: Long): String {
    return if (amount >= 100000) {
        "%.1fL".format(amount / 100000.0)
    } else if (amount >= 1000) {
        "%,d".format(amount)
    } else {
        amount.toString()
    }
}
