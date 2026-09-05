package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentReturn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.ProductEntity
import com.example.ui.theme.Amber100
import com.example.ui.theme.Amber200
import com.example.ui.theme.Amber50
import com.example.ui.theme.Amber500
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

@Composable
fun ProductDetailScreen(
    product: ProductEntity,
    isWishlisted: Boolean,
    onBackClick: () -> Unit,
    onWishlistToggle: (ProductEntity) -> Unit,
    onAddToCart: (ProductEntity, String, Int, Int) -> Unit,
    onBuyNow: (ProductEntity, String, Int) -> Unit,
    onShareProduct: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val availableSizes = remember(product) { product.getParsedSizes() }
    var selectedSize by remember { mutableStateOf(availableSizes.firstOrNull() ?: "Free Size") }

    // Meesho Reselling state
    var isReselling by remember { mutableStateOf(false) }
    var customerPriceInput by remember { mutableStateOf((product.price + 150).toString()) }
    val marginProfit = remember(isReselling, customerPriceInput, product.price) {
        if (!isReselling) 0
        else {
            val entered = customerPriceInput.toIntOrNull() ?: product.price
            (entered - product.price).coerceAtLeast(0)
        }
    }

    // Pin code delivery checker
    var pinCodeInput by remember { mutableStateOf("110001") }
    var isPinChecked by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("product_detail_screen")
    ) {
        // Top App Bar
        Surface(
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("product_detail_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Slate900
                    )
                }

                Text(
                    text = product.category,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate900,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { onWishlistToggle(product) },
                    modifier = Modifier.testTag("product_detail_wishlist_button")
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) Rose600 else Slate400
                    )
                }

                IconButton(
                    onClick = { onShareProduct("Check out ${product.title} on V-Mart India for only ₹${product.price}!") },
                    modifier = Modifier.testTag("product_detail_share_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Slate500
                    )
                }
            }
        }

        // Scrollable Body
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.White)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Discount Banner Overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Indigo600)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "${product.discountPercent}% OFF • SPECIAL PRICE",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Product Main Info Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = product.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Price Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "₹${product.price}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Slate900
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "₹${product.mrp}",
                            fontSize = 14.sp,
                            color = Slate400,
                            textDecoration = TextDecoration.LineThrough
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${product.discountPercent}% off",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Rose600
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Rating & Reviews Pill
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Emerald700)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "%.1f".format(product.rating),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${product.reviewCount} Ratings • ${product.unitsSold}+ Sold",
                            fontSize = 12.sp,
                            color = Slate500,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Trust Badges Row
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TrustBadgeItem(
                            icon = Icons.Default.LocalShipping,
                            title = "Free Delivery",
                            subtitle = "All India"
                        )
                        TrustBadgeItem(
                            icon = Icons.Default.Payments,
                            title = "COD Available",
                            subtitle = "Pay on Delivery"
                        )
                        TrustBadgeItem(
                            icon = Icons.Default.AssignmentReturn,
                            title = "7-Day Return",
                            subtitle = "Easy & Quick"
                        )
                        TrustBadgeItem(
                            icon = Icons.Default.Shield,
                            title = "M-Verified",
                            subtitle = "100% Quality"
                        )
                    }
                }
            }

            // Size Selector Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Select Size",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Text(
                            text = "Size Chart",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Indigo600
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        availableSizes.forEach { size ->
                            val isSelected = selectedSize == size
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) Indigo50 else Color.White,
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) Indigo600 else Slate200
                                ),
                                modifier = Modifier
                                    .testTag("size_chip_$size")
                                    .clickable { selectedSize = size }
                            ) {
                                Text(
                                    text = size,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Indigo600 else Slate700,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Reselling & Margin Tool
            Card(
                colors = CardDefaults.cardColors(containerColor = Amber50),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Amber200),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = Amber700,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Reselling this product? (Earn with V-Mart)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Text(
                                text = "Add your profit margin and ship directly to customer",
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        }
                        Switch(
                            checked = isReselling,
                            onCheckedChange = { isReselling = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Indigo600
                            ),
                            modifier = Modifier.testTag("reselling_toggle")
                        )
                    }

                    if (isReselling) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Amber200)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Your Customer Price (₹)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Slate500
                                )
                                OutlinedTextField(
                                    value = customerPriceInput,
                                    onValueChange = { customerPriceInput = it.filter { char -> char.isDigit() } },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .testTag("customer_price_input")
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Emerald50,
                                border = BorderStroke(1.dp, Emerald100),
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "Your Profit",
                                        fontSize = 10.sp,
                                        color = Emerald700,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "₹$marginProfit",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Emerald700
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // PIN Code Checker Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Check Delivery & COD Availability",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = pinCodeInput,
                            onValueChange = { if (it.length <= 6) pinCodeInput = it },
                            placeholder = { Text("Enter 6-digit PIN code", fontSize = 12.sp, color = Slate400) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("pincode_check_input")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { isPinChecked = pinCodeInput.length == 6 },
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("pincode_check_button")
                        ) {
                            Text("Check", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    if (isPinChecked && pinCodeInput.length == 6) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Emerald700,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Delivery in 3-4 days • Free Shipping • COD Available",
                                fontSize = 12.sp,
                                color = Emerald700,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Product Details & Specifications
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Product Details",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = product.description,
                        fontSize = 13.sp,
                        color = Slate500,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(14.dp))

                    SpecRow(label = "Fabric / Material", value = product.material)
                    SpecRow(label = "Pattern / Design", value = product.pattern)
                    SpecRow(label = "Available Sizes", value = product.sizes)
                    SpecRow(label = "Return Policy", value = product.returnPolicy)
                    SpecRow(label = "Supplier", value = "${product.supplierName} (${product.supplierRating}★)")
                }
            }

            // Customer Ratings & Reviews Breakdown
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Customer Reviews & Ratings",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Text(
                                text = "%.1f".format(product.rating),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Row {
                                repeat(5) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Emerald700,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${product.reviewCount} reviews",
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        }

                        // Progress bars
                        Column(modifier = Modifier.weight(1f)) {
                            RatingProgressBar(stars = "5★", percentage = 0.70f)
                            RatingProgressBar(stars = "4★", percentage = 0.18f)
                            RatingProgressBar(stars = "3★", percentage = 0.07f)
                            RatingProgressBar(stars = "2★", percentage = 0.03f)
                            RatingProgressBar(stars = "1★", percentage = 0.02f)
                        }
                    }
                }
            }
        }

        // Sticky Bottom Bar: Add to Cart & Buy Now
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // Add to Cart button
                OutlinedButton(
                    onClick = {
                        onAddToCart(product, selectedSize, 1, marginProfit)
                    },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.5.dp, Indigo600),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("detail_add_to_cart_button")
                ) {
                    Text(
                        text = "Add to Cart",
                        color = Indigo600,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Buy Now button
                Button(
                    onClick = {
                        onBuyNow(product, selectedSize, marginProfit)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("detail_buy_now_button")
                ) {
                    Text(
                        text = "Buy Now",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TrustBadgeItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Indigo50)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Indigo600,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Slate900
        )
        Text(
            text = subtitle,
            fontSize = 9.sp,
            color = Slate500
        )
    }
}

@Composable
fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Slate500,
            modifier = Modifier.width(130.dp)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Slate900,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun RatingProgressBar(stars: String, percentage: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = stars,
            fontSize = 10.sp,
            color = Slate500,
            modifier = Modifier.width(24.dp)
        )
        LinearProgressIndicator(
            progress = { percentage },
            color = Emerald700,
            trackColor = Slate100,
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
        )
    }
}
