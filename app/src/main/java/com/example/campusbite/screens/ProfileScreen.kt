package com.example.campusbite.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusbite.models.Order
import com.example.campusbite.models.OrderStatus
import com.example.campusbite.ui.theme.CampusBiteTheme
import com.example.campusbite.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    val user = authViewModel.currentUser

    val orderHistory = remember {
        listOf(
            Order(orderId = "ORD001", totalAmount = 12.47, status = OrderStatus.COMPLETED, timestamp = System.currentTimeMillis() - 86400000),
            Order(orderId = "ORD002", totalAmount = 8.99, status = OrderStatus.COMPLETED, timestamp = System.currentTimeMillis() - 172800000),
            Order(orderId = "ORD003", totalAmount = 15.50, status = OrderStatus.CANCELLED, timestamp = System.currentTimeMillis() - 259200000)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("👤", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = user?.email ?: "student@university.edu",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Student ID: STU2024${user?.uid?.take(4) ?: "001"}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Order History",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(orderHistory) { order ->
                OrderHistoryCard(order = order)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Logout")
                }
            }
        }
    }
}

@Composable
fun OrderHistoryCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Order #${order.orderId.take(8)}",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = android.text.format.DateFormat.format("MMM dd, yyyy", order.timestamp).toString(),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${order.totalAmount}",
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = when (order.status) {
                        OrderStatus.COMPLETED -> Color(0xFF4CAF50)
                        OrderStatus.CANCELLED -> Color(0xFFF44336)
                        else -> Color(0xFFFF9800)
                    },
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = order.status.name,
                        fontSize = 10.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

// FIXED PREVIEW - Using remember to avoid the warning
@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    CampusBiteTheme {
        Surface {
            val authViewModel = remember { AuthViewModel() }
            ProfileScreen(
                authViewModel = authViewModel,
                onLogout = {}
            )
        }
    }
}