package com.example.campusbite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.campusbite.models.FoodItem
import com.example.campusbite.ui.theme.CampusBiteTheme
import com.example.campusbite.viewmodels.CartViewModel
import com.example.campusbite.viewmodels.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    menuViewModel: MenuViewModel,
    cartViewModel: CartViewModel,
    onNavigateToCart: () -> Unit,
    onNavigateToProfile: () -> Unit,  // ADD THIS PARAMETER
    onFoodItemClick: (FoodItem) -> Unit
) {
    val categories = menuViewModel.getCategories()
    val selectedCategory by menuViewModel.selectedCategory.collectAsState()
    val filteredItems = menuViewModel.getFilteredItems()
    val cartItemCount = cartViewModel.getItemCount()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusBite Menu", fontWeight = FontWeight.Bold) },
                actions = {
                    // Profile Button
                    IconButton(onClick = onNavigateToProfile) {
                        Text("👤", fontSize = 20.sp)
                    }
                    // Cart Button with Badge
                    BadgedBox(badge = { if (cartItemCount > 0) Badge { Text("$cartItemCount") } }) {
                        IconButton(onClick = onNavigateToCart) {
                            Text("🛒", fontSize = 20.sp)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        onClick = { menuViewModel.setCategory(category) },
                        label = { Text(category) },
                        selected = selectedCategory == category,
                        modifier = Modifier
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredItems) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onFoodItemClick(item) },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🍔", fontSize = 30.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = item.description.take(50),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "$${item.price}",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Button(
                                onClick = { cartViewModel.addToCart(item) },
                                modifier = Modifier.size(width = 80.dp, height = 36.dp)
                            ) {
                                Text("Add", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMenuScreen() {
    CampusBiteTheme {
        Surface {
            val menuViewModel = remember { MenuViewModel() }
            val cartViewModel = remember { CartViewModel() }
            MenuScreen(
                menuViewModel = menuViewModel,
                cartViewModel = cartViewModel,
                onNavigateToCart = {},
                onNavigateToProfile = {},
                onFoodItemClick = {}
            )
        }
    }
}