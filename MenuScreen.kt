package com.example.campusbite.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.campusbite.models.FoodItem
import com.example.campusbite.viewmodels.CartViewModel
import com.example.campusbite.viewmodels.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    menuViewModel: MenuViewModel,
    cartViewModel: CartViewModel,
    onNavigateToCart: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onFoodItemClick: (FoodItem) -> Unit
) {
    val selectedCategory by menuViewModel.selectedCategory.collectAsState()
    val categories = menuViewModel.getCategories()
    val filteredItems = menuViewModel.getFilteredItems()
    val cartItemCount = cartViewModel.getItemCount()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Fastfood, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("CampusBite", fontWeight = FontWeight.Bold)
                    }
                }
            )
        },
        bottomBar = {
            CampusBiteBottomBar(
                selected = "menu",
                cartItemCount = cartItemCount,
                onMenuClick = { },
                onCartClick = onNavigateToCart,
                onProfileClick = onNavigateToProfile
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Choose your meal",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
            )

            Text(
                text = "Fresh campus meals priced in Uganda shillings",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            LazyRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { menuViewModel.setCategory(category) },
                        label = { Text(category) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredItems) { item ->
                    FoodCard(
                        item = item,
                        onClick = { onFoodItemClick(item) },
                        onAddToCart = { cartViewModel.addToCart(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun FoodCard(
    item: FoodItem,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .size(92.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )

                Text(
                    text = formatUgx(item.price),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Button(
                onClick = onAddToCart,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Add")
            }
        }
    }
}

@Composable
fun CampusBiteBottomBar(
    selected: String,
    cartItemCount: Int,
    onMenuClick: () -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selected == "menu",
            onClick = onMenuClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Menu") },
            label = { Text("Menu") }
        )

        NavigationBarItem(
            selected = selected == "cart",
            onClick = onCartClick,
            icon = {
                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
                            Badge { Text(cartItemCount.toString()) }
                        }
                    }
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                }
            },
            label = { Text("Cart") }
        )

        NavigationBarItem(
            selected = selected == "profile",
            onClick = onProfileClick,
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

fun formatUgx(amount: Double): String {
    return "UGX ${amount.toInt()}"
}
