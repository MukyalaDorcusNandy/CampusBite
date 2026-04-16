package com.example.campusbite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun FoodDetailsScreen(
    foodItem: FoodItem,
    cartViewModel: CartViewModel,
    onBack: () -> Unit
) {
    var quantity by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(foodItem.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", fontSize = 24.sp)
                    }
                }
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text("🍽️", fontSize = 80.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = foodItem.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = String.format(Locale.US, "$$%.2f", foodItem.price),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = foodItem.description,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Quantity: ", fontSize = 16.sp)

                IconButton(onClick = { if (quantity > 1) quantity-- }) {
                    Text("−", fontSize = 24.sp)
                }

                Text(
                    text = "$quantity",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                IconButton(onClick = { quantity++ }) {
                    Text("+", fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    repeat(quantity) {
                        cartViewModel.addToCart(foodItem)
                    }
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Add to Cart - ${String.format(Locale.US, "$$%.2f", foodItem.price * quantity)}")
            }
        }
    }
}

// FIXED PREVIEW
@Preview(showBackground = true)
@Composable
fun PreviewFoodDetailsScreen() {
    CampusBiteTheme {
        Surface {
            val cartViewModel = remember { CartViewModel() }
            FoodDetailsScreen(
                foodItem = FoodItem(
                    id = "1",
                    name = "Sample Burger",
                    price = 5.99,
                    description = "Delicious sample burger for preview",
                    category = "Main"
                ),
                cartViewModel = cartViewModel,
                onBack = {}
            )
        }
    }
}