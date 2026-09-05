package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.CartItemWithProduct
import com.example.ui.theme.Amber100
import com.example.ui.theme.Amber50
import com.example.ui.theme.Amber500
import com.example.ui.theme.Amber700
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Indigo100
import com.example.ui.theme.Indigo50
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Indigo700
import com.example.ui.theme.Rose50
import com.example.ui.theme.Rose500
import com.example.ui.theme.Rose600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun CartScreen(
    cartItems: List<CartItemWithProduct>,
    appliedCoupon: String?,
    couponDiscount: Int,
    onBackClick: () -> Unit,
    onUpdateQuantity: (cartId: Int, newQuantity: Int) -> Unit,
    onRemoveItem: (cartId: Int) -> Unit,
    onApplyCoupon: (String) -> Boolean,
    onRemoveCoupon: () -> Unit,
    onProceedToCheckout: () -> Unit,
    onExploreProducts: () -> Unit,
    modifier: Modifier = Modifier
) {
    var couponInput by remember { mutableStateOf("") }

    val rawTotal = cartItems.sumOf { it.itemTotal }
    val mrpTotal = cartItems.sumOf { it.mrpTotal }
    val totalDiscount = (mrpTotal - rawTotal).coerceAtLeast(0)
    val finalPayable = (rawTotal - couponDiscount).coerceAtLeast(0)
    val totalSavings = totalDiscount + couponDiscount

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("cart_screen")
    ) {
        // Top Bar
        Surface(
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("cart_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Slate900
                    )
                }
                Text(
                    text = "Shopping Cart (${cartItems.size})",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
            }
        }

        if (cartItems.isEmpty()) {
            // Empty Cart State
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.RemoveShoppingCart,
                    contentDescription = null,
                    tint = Slate400,
                    modifier = Modifier.size(72.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your Cart is Empty!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Discover lowest prices and wholesale deals on Indian sarees, kurtis, electronics and more.",
                    fontSize = 13.sp,
                    color = Slate500,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onExploreProducts,
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("empty_cart_shop_button")
                ) {
                    Text("Start Shopping Now", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            // Cart Items & Price Breakdown List
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Cart Items
                items(cartItems, key = { it.cartItem.id }) { item ->
                    CartItemCard(
                        item = item,
                        onUpdateQuantity = onUpdateQuantity,
                        onRemoveItem = onRemoveItem
                    )
                }

                // Coupon Code Section
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Slate200),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = null,
                                    tint = Indigo600,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Coupons & Offers",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            if (appliedCoupon != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Emerald50)
                                        .border(BorderStroke(1.dp, Emerald100), RoundedCornerShape(10.dp))
                                        .padding(horizontal = 12.dp, vertical = 10.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Emerald600,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "'$appliedCoupon' Applied (-₹$couponDiscount)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Emerald700
                                        )
                                    }
                                    Text(
                                        text = "Remove",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Rose600,
                                        modifier = Modifier
                                            .clickable { onRemoveCoupon() }
                                            .testTag("remove_coupon_button")
                                    )
                                }
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = couponInput,
                                        onValueChange = { couponInput = it.uppercase() },
                                        placeholder = { Text("Enter Coupon Code", fontSize = 12.sp, color = Slate400) },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Indigo600,
                                            unfocusedBorderColor = Slate300
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(50.dp)
                                            .testTag("coupon_text_input")
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            if (onApplyCoupon(couponInput)) {
                                                couponInput = ""
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .height(50.dp)
                                            .testTag("apply_coupon_button")
                                    ) {
                                        Text("Apply", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Quick coupon chips
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Indigo50,
                                        border = BorderStroke(1.dp, Indigo100),
                                        modifier = Modifier
                                            .clickable { onApplyCoupon("VMART100") }
                                            .testTag("coupon_chip_vmart100")
                                    ) {
                                        Text(
                                            text = "VMART100 (₹100 OFF)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Indigo700,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                        )
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Amber50,
                                        border = BorderStroke(1.dp, Amber100),
                                        modifier = Modifier
                                            .clickable { onApplyCoupon("MAHASALE") }
                                            .testTag("coupon_chip_mahasale")
                                    ) {
                                        Text(
                                            text = "MAHASALE (₹150 OFF)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Amber700,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Price Summary Card
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Slate200),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Price Details (${cartItems.sumOf { it.cartItem.quantity }} Items)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            PriceSummaryLine("Total Product MRP", "₹$mrpTotal")
                            PriceSummaryLine("Discount on MRP", "-₹$totalDiscount", isDiscount = true)
                            PriceSummaryLine("Delivery Charges", "FREE", isGreen = true)

                            if (couponDiscount > 0) {
                                PriceSummaryLine("Coupon Discount", "-₹$couponDiscount", isDiscount = true)
                            }

                            val totalResellerMargins = cartItems.sumOf { it.cartItem.resellerMargin * it.cartItem.quantity }
                            if (totalResellerMargins > 0) {
                                PriceSummaryLine("Reseller Margin (Your Profit)", "+₹$totalResellerMargins", isSaffron = true)
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = Slate100)
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Total Payable Amount",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Text(
                                    text = "₹$finalPayable",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Slate900
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Total Savings Pill
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Emerald50,
                                border = BorderStroke(1.dp, Emerald100),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "🎉 You will save ₹$totalSavings on this order!",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Emerald700,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                }

                // Trust footer
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = Emerald600,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Safe & Secure Payments • 100% Authentic Products",
                            fontSize = 12.sp,
                            color = Slate500,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Sticky Bottom Checkout Bar
            Surface(
                color = Color.White,
                shadowElevation = 6.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Column {
                        Text(
                            text = "Total Amount",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                        Text(
                            text = "₹$finalPayable",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Slate900
                        )
                    }

                    Button(
                        onClick = onProceedToCheckout,
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(46.dp)
                            .testTag("cart_checkout_button")
                    ) {
                        Text(
                            text = "Continue to Checkout",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItemWithProduct,
    onUpdateQuantity: (cartId: Int, newQuantity: Int) -> Unit,
    onRemoveItem: (cartId: Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Slate200),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("cart_item_${item.cartItem.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Image
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Slate100)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.product.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = item.product.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.product.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Slate900,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Size: ${item.cartItem.selectedSize}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Indigo600
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "₹${item.product.price + item.cartItem.resellerMargin}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Slate900
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "₹${item.product.mrp}",
                            fontSize = 11.sp,
                            color = Slate400,
                            textDecoration = TextDecoration.LineThrough
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${item.product.discountPercent}% off",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Rose600
                        )
                    }

                    if (item.cartItem.resellerMargin > 0) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Includes ₹${item.cartItem.resellerMargin} Reseller Margin",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Amber700
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Slate100)
            Spacer(modifier = Modifier.height(8.dp))

            // Bottom Actions: Quantity control & Delete
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Quantity buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Slate100)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    IconButton(
                        onClick = { onUpdateQuantity(item.cartItem.id, item.cartItem.quantity - 1) },
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("qty_minus_${item.cartItem.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease",
                            modifier = Modifier.size(16.dp),
                            tint = Slate800
                        )
                    }

                    Text(
                        text = item.cartItem.quantity.toString(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                    IconButton(
                        onClick = { onUpdateQuantity(item.cartItem.id, item.cartItem.quantity + 1) },
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("qty_plus_${item.cartItem.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase",
                            modifier = Modifier.size(16.dp),
                            tint = Slate800
                        )
                    }
                }

                // Remove button
                IconButton(
                    onClick = { onRemoveItem(item.cartItem.id) },
                    modifier = Modifier.testTag("remove_item_${item.cartItem.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove item",
                        tint = Slate400,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PriceSummaryLine(
    label: String,
    value: String,
    isDiscount: Boolean = false,
    isGreen: Boolean = false,
    isSaffron: Boolean = false
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Slate500
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = if (isDiscount || isGreen) FontWeight.Bold else FontWeight.Medium,
            color = when {
                isDiscount -> Rose600
                isGreen -> Emerald700
                isSaffron -> Amber700
                else -> Slate900
            }
        )
    }
}
