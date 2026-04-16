package com.example.campusbite.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusbite.models.FoodItem
import com.example.campusbite.ui.theme.CampusBiteTheme
import com.example.campusbite.viewmodels.CartViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    onConfirmOrder: () -> Unit,
    onBack: () -> Unit,
    userId: String
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalPrice = cartViewModel.getTotalPrice()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", fontSize = 24.sp)
                    }
                }
            )
        }
    ) { paddingValues ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🛒", fontSize = 64.sp)
                    Text("Your cart is empty", fontSize = 18.sp)
                    TextButton(onClick = onBack) {
                        Text("Browse Menu")
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartItems) { cartItem ->
                        CartItemRow(
                            cartItem = cartItem,
                            onDecrease = {
                                cartViewModel.updateQuantity(cartItem, cartItem.quantity - 1)
                            },
                            onIncrease = {
                                cartViewModel.updateQuantity(cartItem, cartItem.quantity + 1)
                            }
                        )
                    }
                }

                HorizontalDivider(thickness = 1.dp)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(
                            formatPrice(totalPrice),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            cartViewModel.placeOrder(userId) {
                                onConfirmOrder()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Confirm Order", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: com.example.campusbite.models.CartItem,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartItem.foodItem.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = formatPrice(cartItem.foodItem.price) + " each",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrease) {
                    Text("−", fontSize = 20.sp)
                }

                Text(
                    text = "${cartItem.quantity}",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                IconButton(onClick = onIncrease) {
                    Text("+", fontSize = 20.sp)
                }
            }

            Text(
                text = formatPrice(cartItem.totalPrice),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

private fun formatPrice(price: Double): String {
    return String.format(Locale.US, "$$%.2f", price)
}

// FIXED PREVIEW
@Preview(showBackground = true)
@Composable
fun PreviewCartScreen() {
    CampusBiteTheme {
        Surface {
            val sampleCartViewModel = remember { CartViewModel() }
            sampleCartViewModel.addToCart(
                FoodItem(
                    id = "1",
                    name = "Burger",
                    price = 5.99,
                    description = "Tasty beef burger",
                    category = "Main"
                )
            )
            sampleCartViewModel.addToCart(
                FoodItem(
                    id = "2",
                    name = "Pizza Slice",
                    price = 3.99,
                    description = "Cheese pizza",
                    category = "Main"
                )
            )
            CartScreen(
                cartViewModel = sampleCartViewModel,
                onConfirmOrder = {},
                onBack = {},
                userId = "preview123"
            )
        }
    }
}