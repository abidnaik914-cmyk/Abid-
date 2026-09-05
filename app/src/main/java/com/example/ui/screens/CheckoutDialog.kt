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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Indigo50
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900

@Composable
fun CheckoutDialog(
    totalAmount: Int,
    onDismiss: () -> Unit,
    onConfirmOrder: (
        name: String,
        phone: String,
        pin: String,
        city: String,
        state: String,
        address: String,
        paymentMethod: String
    ) -> Unit
) {
    var name by remember { mutableStateOf("Abid Naik") }
    var phone by remember { mutableStateOf("9876543210") }
    var pin by remember { mutableStateOf("110001") }
    var city by remember { mutableStateOf("New Delhi") }
    var state by remember { mutableStateOf("Delhi") }
    var address by remember { mutableStateOf("Plot 42, Connaught Place, Central Delhi") }

    var selectedPayment by remember { mutableStateOf("Cash on Delivery") }

    // Auto update city/state based on popular Indian PIN codes
    fun onPinChanged(newPin: String) {
        if (newPin.length <= 6) {
            pin = newPin
            when (newPin) {
                "110001" -> { city = "New Delhi"; state = "Delhi" }
                "400001" -> { city = "Mumbai"; state = "Maharashtra" }
                "560001" -> { city = "Bengaluru"; state = "Karnataka" }
                "700001" -> { city = "Kolkata"; state = "West Bengal" }
                "600001" -> { city = "Chennai"; state = "Tamil Nadu" }
                "500001" -> { city = "Hyderabad"; state = "Telangana" }
                "226001" -> { city = "Lucknow"; state = "Uttar Pradesh" }
                "800001" -> { city = "Patna"; state = "Bihar" }
                "302001" -> { city = "Jaipur"; state = "Rajasthan" }
                "380001" -> { city = "Ahmedabad"; state = "Gujarat" }
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Slate200),
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 24.dp)
                .testTag("checkout_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Easy & Simple Checkout",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Text(
                            text = "Free Home Delivery across all India",
                            fontSize = 11.sp,
                            color = Emerald700,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_checkout_dialog")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Slate500)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Slate200)
                Spacer(modifier = Modifier.height(12.dp))

                // Section 1: Delivery Address
                Text(
                    text = "1. Delivery Address",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Customer Full Name") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_name_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { if (it.length <= 10) phone = it },
                    label = { Text("10-Digit Mobile Number") },
                    prefix = { Text("+91 ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_phone_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = pin,
                        onValueChange = { onPinChanged(it) },
                        label = { Text("PIN Code") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("checkout_pin_input")
                    )
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1.2f)
                            .testTag("checkout_city_input")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state,
                    onValueChange = { state = it },
                    label = { Text("State") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("House No, Building, Road, Landmark") },
                    maxLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_address_input")
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Slate200)
                Spacer(modifier = Modifier.height(12.dp))

                // Section 2: Payment Method
                Text(
                    text = "2. Select Payment Mode",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Spacer(modifier = Modifier.height(8.dp))

                PaymentOptionItem(
                    title = "Cash on Delivery (COD)",
                    subtitle = "Pay with Cash or UPI when package arrives",
                    icon = Icons.Default.Payments,
                    isSelected = selectedPayment == "Cash on Delivery",
                    onSelect = { selectedPayment = "Cash on Delivery" },
                    tag = "payment_cod"
                )

                PaymentOptionItem(
                    title = "UPI (PhonePe / Google Pay / Paytm)",
                    subtitle = "Fast, secure & instant zero-fee transfer",
                    icon = Icons.Default.QrCodeScanner,
                    isSelected = selectedPayment == "UPI (PhonePe/GPay)",
                    onSelect = { selectedPayment = "UPI (PhonePe/GPay)" },
                    tag = "payment_upi"
                )

                PaymentOptionItem(
                    title = "Debit / Credit Card",
                    subtitle = "Visa, MasterCard, RuPay accepted",
                    icon = Icons.Default.CreditCard,
                    isSelected = selectedPayment == "Debit/Credit Card",
                    onSelect = { selectedPayment = "Debit/Credit Card" },
                    tag = "payment_card"
                )

                PaymentOptionItem(
                    title = "Net Banking",
                    subtitle = "SBI, HDFC, ICICI, Axis and 50+ banks",
                    icon = Icons.Default.AccountBalance,
                    isSelected = selectedPayment == "Net Banking",
                    onSelect = { selectedPayment = "Net Banking" },
                    tag = "payment_netbanking"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Payable summary pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Emerald50,
                    border = BorderStroke(1.dp, Emerald100),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "Total to Pay",
                                fontSize = 11.sp,
                                color = Emerald700,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Free Delivery Included",
                                fontSize = 10.sp,
                                color = Slate500
                            )
                        }
                        Text(
                            text = "₹$totalAmount",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Emerald700
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Place Order Button
                Button(
                    onClick = {
                        if (name.isNotBlank() && phone.isNotBlank() && address.isNotBlank()) {
                            onConfirmOrder(name, phone, pin, city, state, address, selectedPayment)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("confirm_place_order_button")
                ) {
                    Text(
                        text = "Confirm & Place Order",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = Slate400,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "V-Mart 100% Safe Purchase Guarantee",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentOptionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit,
    tag: String
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) Indigo50 else Slate100,
        border = BorderStroke(
            1.dp,
            if (isSelected) Indigo600 else Slate200
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onSelect() }
            .testTag(tag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(selectedColor = Indigo600)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Indigo600 else Slate500,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = Slate900
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Slate500
                )
            }
        }
    }
}
