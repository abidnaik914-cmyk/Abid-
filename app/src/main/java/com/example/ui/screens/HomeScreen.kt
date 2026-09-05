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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.InitialData
import com.example.data.model.ProductEntity
import com.example.ui.components.FestiveHeroBanner
import com.example.ui.components.ProductCard
import com.example.ui.theme.Amber100
import com.example.ui.theme.Amber50
import com.example.ui.theme.Amber500
import com.example.ui.theme.Amber700
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Indigo50
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Rose100
import com.example.ui.theme.Rose50
import com.example.ui.theme.Rose500
import com.example.ui.theme.Rose700
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.SortOption

@Composable
fun HomeScreen(
    products: List<ProductEntity>,
    wishlistIds: Set<Int>,
    selectedCategory: String,
    selectedSort: SortOption,
    under199Only: Boolean,
    minRatingFilter: Float,
    searchQuery: String,
    onCategorySelected: (String) -> Unit,
    onSortSelected: (SortOption) -> Unit,
    onToggleUnder199: () -> Unit,
    onSetRatingFilter: (Float) -> Unit,
    onResetFilters: () -> Unit,
    onProductClick: (ProductEntity) -> Unit,
    onWishlistToggle: (ProductEntity) -> Unit,
    onApplyCoupon: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 80.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("home_screen_grid")
    ) {
        // 1. Promotional Hero & Reseller Banner (Span across full width)
        item(span = { GridItemSpan(2) }) {
            FestiveHeroBanner(
                onApplyCoupon = onApplyCoupon,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        // 2. Value Highlights row (Big Savings, Free Delivery, Low Price)
        item(span = { GridItemSpan(2) }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 2.dp)
            ) {
                // Big Savings / Under 199 store (Rose styling)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (under199Only) Rose100 else Rose50,
                    border = BorderStroke(1.dp, Rose100),
                    modifier = Modifier
                        .testTag("filter_under_199")
                        .clickable { onToggleUnder199() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Percent,
                            contentDescription = null,
                            tint = Rose500,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (under199Only) "Under ₹199 (Active)" else "Big Savings",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Rose700
                        )
                    }
                }

                // Free Delivery Badge (Amber styling)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Amber50,
                    border = BorderStroke(1.dp, Amber100),
                    modifier = Modifier
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = Amber500,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Free Delivery",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Amber700
                        )
                    }
                }

                // Low Price / 4★ & Above (Emerald styling)
                val is4StarActive = minRatingFilter >= 4.0f
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (is4StarActive) Emerald100 else Emerald50,
                    border = BorderStroke(1.dp, Emerald100),
                    modifier = Modifier
                        .testTag("filter_rating_4plus")
                        .clickable { onSetRatingFilter(if (is4StarActive) 0f else 4.0f) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = Emerald500,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (is4StarActive) "4.0★ Top Rated" else "Low Price",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Emerald700
                        )
                    }
                }

                // Sort Dropdown Pill
                Box {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Slate200),
                        modifier = Modifier
                            .testTag("sort_dropdown_trigger")
                            .clickable { sortMenuExpanded = true }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = null,
                                tint = Slate600,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = selectedSort.displayName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Slate700
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Slate400,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = sortMenuExpanded,
                        onDismissRequest = { sortMenuExpanded = false }
                    ) {
                        SortOption.values().forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = option.displayName,
                                        fontSize = 13.sp,
                                        fontWeight = if (selectedSort == option) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selectedSort == option) Indigo600 else Slate900
                                    )
                                },
                                onClick = {
                                    onSortSelected(option)
                                    sortMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // 3. Category Filter Chips (Horizontal scroll)
        item(span = { GridItemSpan(2) }) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "Top Categories",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 4.dp)
                ) {
                    InitialData.categories.forEach { category ->
                        val isSelected = selectedCategory.equals(category, ignoreCase = true)
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) Indigo600 else Color.White,
                            border = BorderStroke(1.dp, if (isSelected) Indigo600 else Slate200),
                            shadowElevation = if (isSelected) 1.dp else 0.dp,
                            modifier = Modifier
                                .testTag("category_chip_$category")
                                .clickable { onCategorySelected(category) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = category,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Slate700
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Products Count & Result Header
        item(span = { GridItemSpan(2) }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Text(
                    text = "Popular Products (${products.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate800
                )

                if (selectedCategory != "All" || under199Only || minRatingFilter > 0f || searchQuery.isNotBlank()) {
                    Text(
                        text = "Reset All",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Indigo600,
                        modifier = Modifier
                            .clickable { onResetFilters() }
                            .testTag("reset_filters_button")
                    )
                } else {
                    Text(
                        text = "See All",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Indigo600
                    )
                }
            }
        }

        // 5. Empty State or Products Grid
        if (products.isEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SearchOff,
                        contentDescription = null,
                        tint = Slate400,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No products found",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Text(
                        text = "Try adjusting your search or filters to see more deals",
                        fontSize = 12.sp,
                        color = Slate500,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onResetFilters,
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Show All Products", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            items(products, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    isWishlisted = wishlistIds.contains(product.id),
                    onProductClick = onProductClick,
                    onWishlistToggle = onWishlistToggle
                )
            }
        }
    }
}
