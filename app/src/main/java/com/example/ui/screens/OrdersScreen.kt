package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderEntity
import com.example.ui.theme.Amber100
import com.example.ui.theme.Amber50
import com.example.ui.theme.Amber500
import com.example.ui.theme.Amber700
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Indigo50
import com.example.ui.theme.Indigo600
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrdersScreen(
    orders: List<OrderEntity>,
    onExploreShop: () -> Unit,
    onDownloadInvoice: (OrderEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("orders_screen")
    ) {
        // Top Header
        Surface(
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                Text(
                    text = "My Orders & Tracking",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Text(
                    text = "Track live delivery status & view purchase invoices",
                    fontSize = 12.sp,
                    color = Slate500
                )
            }
        }

        if (orders.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ShoppingBag,
                    contentDescription = null,
                    tint = Slate400,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No Orders Placed Yet",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Shop top quality products at manufacturer prices with free home delivery.",
                    fontSize = 13.sp,
                    color = Slate500,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onExploreShop,
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Explore Products", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("orders_list")
            ) {
                items(orders, key = { it.orderId }) { order ->
                    OrderItemCard(
                        order = order,
                        onDownloadInvoice = { onDownloadInvoice(order) }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderItemCard(
    order: OrderEntity,
    onDownloadInvoice: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH)
    val formattedDate = dateFormat.format(Date(order.orderTimestamp))

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Slate200),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("order_card_${order.orderId}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Order ID & Status Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Order #${order.orderId}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Text(
                        text = formattedDate,
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }

                // Status Badge
                val (statusBg, statusFg) = when (order.currentStatus) {
                    "DELIVERED" -> Pair(Emerald50, Emerald700)
                    "OUT_FOR_DELIVERY" -> Pair(Indigo50, Indigo600)
                    "SHIPPED" -> Pair(Amber50, Amber700)
                    else -> Pair(Slate100, Slate700)
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = statusBg
                ) {
                    Text(
                        text = order.currentStatus.replace("_", " "),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusFg,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Slate100)
            Spacer(modifier = Modifier.height(10.dp))

            // Items Summary
            Text(
                text = order.itemsSummary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Slate900,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Address summary
            Text(
                text = "Deliver to: ${order.customerName}, ${order.addressCity} - ${order.addressPin}",
                fontSize = 12.sp,
                color = Slate500
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Amount & Payment row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Total Paid: ",
                        fontSize = 12.sp,
                        color = Slate500
                    )
                    Text(
                        text = "₹${order.totalAmount}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Slate100
                    ) {
                        Text(
                            text = order.paymentMethod,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate700,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                if (order.resellerProfitTotal > 0) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Amber50,
                        border = BorderStroke(1.dp, Amber100)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = Amber700,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Margin: +₹${order.resellerProfitTotal}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Amber700
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Live Tracking Stepper
            LiveTrackingStepper(currentStatus = order.currentStatus)

            Spacer(modifier = Modifier.height(14.dp))

            // Actions Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onDownloadInvoice,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Slate300),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = Slate700,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Invoice", fontSize = 12.sp, color = Slate700, fontWeight = FontWeight.SemiBold)
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Emerald50,
                    border = BorderStroke(1.dp, Emerald100),
                    modifier = Modifier.weight(1.5f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = Emerald600,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = order.deliveryDateEst,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Emerald700
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LiveTrackingStepper(currentStatus: String) {
    val steps = listOf("PLACED", "SHIPPED", "OUT_FOR_DELIVERY", "DELIVERED")
    val labels = listOf("Placed", "Shipped", "Out for Delivery", "Delivered")
    val currentIndex = steps.indexOf(currentStatus).coerceAtLeast(0)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        labels.forEachIndexed { index, label ->
            val isDone = index <= currentIndex
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(if (isDone) Emerald600 else Slate200)
                ) {
                    if (isDone) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    fontSize = 9.sp,
                    fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal,
                    color = if (isDone) Slate900 else Slate400,
                    maxLines = 1
                )
            }

            if (index < labels.size - 1) {
                Box(
                    modifier = Modifier
                        .weight(0.6f)
                        .height(2.dp)
                        .background(if (index < currentIndex) Emerald600 else Slate200)
                )
            }
        }
    }
}
