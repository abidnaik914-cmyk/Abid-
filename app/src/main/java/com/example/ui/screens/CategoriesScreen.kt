package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Woman
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProductEntity
import com.example.ui.theme.Amber100
import com.example.ui.theme.Amber50
import com.example.ui.theme.Amber700
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Indigo100
import com.example.ui.theme.Indigo50
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Rose50
import com.example.ui.theme.Rose600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900

data class CategoryMeta(
    val name: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val iconTintColor: Color,
    val subcategories: List<String>
)

@Composable
fun CategoriesScreen(
    allProducts: List<ProductEntity>,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoriesMeta = listOf(
        CategoryMeta(
            name = "Women Ethnic",
            subtitle = "Sarees, Kurtis, Lehengas, Suits",
            icon = Icons.Default.Woman,
            iconBgColor = Rose50,
            iconTintColor = Rose600,
            subcategories = listOf("Sarees", "Kurti Sets", "Lehengas", "Dupattas")
        ),
        CategoryMeta(
            name = "Men's Fashion",
            subtitle = "Shirts, Kurtas, T-Shirts, Jeans",
            icon = Icons.Default.Man,
            iconBgColor = Indigo50,
            iconTintColor = Indigo600,
            subcategories = listOf("Casual Shirts", "Ethnic Kurtas", "Jeans", "T-Shirts")
        ),
        CategoryMeta(
            name = "Kids & Baby",
            subtitle = "Rompers, Frocks, Boys Clothing",
            icon = Icons.Default.ChildCare,
            iconBgColor = Amber50,
            iconTintColor = Amber700,
            subcategories = listOf("Baby Clothing", "Girls Wear", "Boys Wear", "Toys")
        ),
        CategoryMeta(
            name = "Home & Kitchen",
            subtitle = "Choppers, Bedsheets, Cookware, Storage",
            icon = Icons.Default.Home,
            iconBgColor = Emerald50,
            iconTintColor = Emerald700,
            subcategories = listOf("Kitchen Tools", "Home Furnishing", "Cookware", "Storage")
        ),
        CategoryMeta(
            name = "Electronics",
            subtitle = "Bluetooth Neckbands, Smartwatches, Cables",
            icon = Icons.Default.Devices,
            iconBgColor = Indigo50,
            iconTintColor = Indigo600,
            subcategories = listOf("Audio", "Wearables", "Cables", "Power Banks")
        ),
        CategoryMeta(
            name = "Beauty",
            subtitle = "Lipsticks, Skincare, Makeup Combos",
            icon = Icons.Default.Face,
            iconBgColor = Rose50,
            iconTintColor = Rose600,
            subcategories = listOf("Makeup", "Skincare", "Haircare", "Combos")
        ),
        CategoryMeta(
            name = "Footwear",
            subtitle = "Sneakers, Sports Shoes, Sandals",
            icon = Icons.Default.ShoppingBag,
            iconBgColor = Indigo50,
            iconTintColor = Indigo600,
            subcategories = listOf("Men Footwear", "Women Footwear", "Casual", "Sports")
        )
    )

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("categories_screen_list")
    ) {
        item {
            Column(modifier = Modifier.padding(bottom = 4.dp)) {
                Text(
                    text = "Explore All Categories",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Text(
                    text = "Direct from trusted Indian wholesale manufacturers & suppliers",
                    fontSize = 12.sp,
                    color = Slate500
                )
            }
        }

        items(categoriesMeta) { cat ->
            val count = allProducts.count { it.category.equals(cat.name, ignoreCase = true) }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Slate200),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("category_card_${cat.name}")
                    .clickable { onCategorySelected(cat.name) }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Category Icon
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(cat.iconBgColor)
                        ) {
                            Icon(
                                imageVector = cat.icon,
                                contentDescription = cat.name,
                                tint = cat.iconTintColor,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = cat.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Slate100
                                ) {
                                    Text(
                                        text = "$count items",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Slate700,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = cat.subtitle,
                                fontSize = 12.sp,
                                color = Slate500
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "View",
                            tint = Slate400,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Subcategory pill tags
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        cat.subcategories.take(4).forEach { sub ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Slate100,
                                border = BorderStroke(1.dp, Slate200),
                                modifier = Modifier.clickable { onCategorySelected(cat.name) }
                            ) {
                                Text(
                                    text = sub,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Slate700,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
