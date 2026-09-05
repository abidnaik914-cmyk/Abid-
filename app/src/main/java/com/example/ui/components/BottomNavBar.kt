package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Indigo50
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate400
import com.example.ui.viewmodel.Screen

@Composable
fun BottomNavBar(
    currentScreen: Screen,
    orderCount: Int,
    onScreenSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(thickness = 1.dp, color = Slate100)
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp,
            windowInsets = WindowInsets.navigationBars,
            modifier = Modifier.testTag("bottom_nav_bar")
        ) {
            // 1. Home
            val isShopActive = currentScreen == Screen.HOME || currentScreen == Screen.PRODUCT_DETAIL
            NavigationBarItem(
                selected = isShopActive,
                onClick = { onScreenSelected(Screen.HOME) },
                icon = {
                    Icon(
                        imageVector = if (isShopActive) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Home",
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = "Home",
                        fontSize = 11.sp,
                        fontWeight = if (isShopActive) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Indigo600,
                    selectedTextColor = Indigo600,
                    indicatorColor = Indigo50,
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("nav_tab_shop")
            )

            // 2. Categories
            val isCatActive = currentScreen == Screen.CATEGORIES
            NavigationBarItem(
                selected = isCatActive,
                onClick = { onScreenSelected(Screen.CATEGORIES) },
                icon = {
                    Icon(
                        imageVector = if (isCatActive) Icons.Filled.Category else Icons.Outlined.Category,
                        contentDescription = "Categories",
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = "Categories",
                        fontSize = 11.sp,
                        fontWeight = if (isCatActive) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Indigo600,
                    selectedTextColor = Indigo600,
                    indicatorColor = Indigo50,
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("nav_tab_categories")
            )

            // 3. Analytics (Data Analytics)
            val isAnalyticsActive = currentScreen == Screen.ANALYTICS
            NavigationBarItem(
                selected = isAnalyticsActive,
                onClick = { onScreenSelected(Screen.ANALYTICS) },
                icon = {
                    Icon(
                        imageVector = if (isAnalyticsActive) Icons.Filled.Assessment else Icons.Outlined.Assessment,
                        contentDescription = "Analytics",
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = "Business",
                        fontSize = 11.sp,
                        fontWeight = if (isAnalyticsActive) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Indigo600,
                    selectedTextColor = Indigo600,
                    indicatorColor = Indigo50,
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("nav_tab_analytics")
            )

            // 4. Orders
            val isOrdersActive = currentScreen == Screen.ORDERS
            NavigationBarItem(
                selected = isOrdersActive,
                onClick = { onScreenSelected(Screen.ORDERS) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (orderCount > 0) {
                                Badge(
                                    containerColor = Indigo600,
                                    contentColor = Color.White
                                ) {
                                    Text(orderCount.toString(), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isOrdersActive) Icons.Filled.ReceiptLong else Icons.Outlined.ReceiptLong,
                            contentDescription = "My Orders",
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = "Orders",
                        fontSize = 11.sp,
                        fontWeight = if (isOrdersActive) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Indigo600,
                    selectedTextColor = Indigo600,
                    indicatorColor = Indigo50,
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("nav_tab_orders")
            )
        }
    }
}
