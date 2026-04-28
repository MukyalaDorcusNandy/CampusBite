package com.example.campusbite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusbite.models.OrderStatus
import com.example.campusbite.ui.theme.CampusBiteTheme
import com.example.campusbite.viewmodels.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderStatusScreen(
    cartViewModel: CartViewModel,
    onBackToMenu: () -> Unit
) {
    val orderStatus by cartViewModel.orderStatus.collectAsState()
    val currentOrder by cartViewModel.currentOrder.collectAsState()

    val statusSteps = listOf(
        OrderStatus.PENDING to "Order Received",
        OrderStatus.CONFIRMED to "Confirmed",
        OrderStatus.PREPARING to "Preparing",
        OrderStatus.READY_FOR_PICKUP to "Ready for Pickup"
    )

    val currentStepIndex = orderStatus?.let { status ->
        statusSteps.indexOfFirst { it.first == status }.coerceAtLeast(0)
    } ?: 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Status") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🍕", fontSize = 60.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = when (orderStatus) {
                    OrderStatus.PENDING -> "Order Placed!"
                    OrderStatus.CONFIRMED -> "Order Confirmed!"
                    OrderStatus.PREPARING -> "Preparing Your Food!"
                    OrderStatus.READY_FOR_PICKUP -> "Ready for Pickup!"
                    else -> "Processing Your Order"
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            currentOrder?.let { order ->
                Text(
                    text = "Estimated pickup: ${order.estimatedPickupTime}",
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            statusSteps.forEachIndexed { index, (_, label) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                if (index <= currentStepIndex)
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color.LightGray
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (index < currentStepIndex) {
                            Text("✓", color = Color.White)
                        } else if (index == currentStepIndex) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                        } else {
                            Text("${index + 1}", color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = if (index <= currentStepIndex) FontWeight.Bold else FontWeight.Normal
                    )
                }

                if (index < statusSteps.size - 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            if (orderStatus == OrderStatus.READY_FOR_PICKUP) {
                Button(
                    onClick = {
                        cartViewModel.resetOrder()
                        onBackToMenu()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Menu")
                }
            }

            if (orderStatus != null &&
                orderStatus != OrderStatus.READY_FOR_PICKUP &&
                orderStatus != OrderStatus.CANCELLED
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { cartViewModel.cancelOrder() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Cancel Order")
                }
            }

            if (orderStatus == OrderStatus.CANCELLED) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Order Cancelled",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        cartViewModel.resetOrder()
                        onBackToMenu()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Menu")
                }
            }
        }
    }
}

// FIXED PREVIEW
@Preview(showBackground = true)
@Composable
fun PreviewOrderStatusScreen() {
    CampusBiteTheme {
        Surface {
            val cartViewModel = remember { CartViewModel() }
            OrderStatusScreen(
                cartViewModel = cartViewModel,
                onBackToMenu = {}
            )
        }
    }
}