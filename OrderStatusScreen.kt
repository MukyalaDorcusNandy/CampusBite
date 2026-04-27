package com.example.campusbite.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.campusbite.models.OrderStatus
import com.example.campusbite.viewmodels.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderStatusScreen(
    cartViewModel: CartViewModel,
    onBackToMenu: () -> Unit
) {
    val order by cartViewModel.currentOrder.collectAsState()
    val status by cartViewModel.orderStatus.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Status", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Track your meal",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Estimated pickup: ${order?.estimatedPickupTime ?: "15-20 minutes"}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(28.dp))

            StatusStep(
                title = "Order received",
                subtitle = "Your order has been submitted",
                active = status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED || status == OrderStatus.PREPARING || status == OrderStatus.READY_FOR_PICKUP
            )

            StatusStep(
                title = "Confirmed",
                subtitle = "The cafeteria accepted your order",
                active = status == OrderStatus.CONFIRMED || status == OrderStatus.PREPARING || status == OrderStatus.READY_FOR_PICKUP
            )

            StatusStep(
                title = "Preparing",
                subtitle = "Your food is being prepared",
                active = status == OrderStatus.PREPARING || status == OrderStatus.READY_FOR_PICKUP
            )

            StatusStep(
                title = "Ready for pickup",
                subtitle = "Go to the cafeteria counter",
                active = status == OrderStatus.READY_FOR_PICKUP
            )

            Spacer(modifier = Modifier.weight(1f))

            if (status == OrderStatus.READY_FOR_PICKUP) {
                Button(
                    onClick = {
                        cartViewModel.resetOrder()
                        onBackToMenu()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Back to Menu")
                }
            } else {
                OutlinedButton(
                    onClick = onBackToMenu,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continue Shopping")
                }
            }
        }
    }
}

@Composable
fun StatusStep(
    title: String,
    subtitle: String,
    active: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape),
            color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = if (active) Icons.Default.Check else Icons.Default.Timer,
                    contentDescription = null,
                    tint = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(title, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}
