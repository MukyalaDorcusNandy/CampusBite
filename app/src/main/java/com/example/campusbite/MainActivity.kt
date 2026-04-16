package com.example.campusbite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campusbite.screens.*
import com.example.campusbite.ui.theme.CampusBiteTheme
import com.example.campusbite.viewmodels.AuthViewModel
import com.example.campusbite.viewmodels.AuthState
import com.example.campusbite.viewmodels.CartViewModel
import com.example.campusbite.viewmodels.MenuViewModel
import com.example.campusbite.models.FoodItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampusBiteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CampusBiteApp()
                }
            }
        }
    }
}

@Composable
fun CampusBiteApp() {
    val authViewModel: AuthViewModel = viewModel()
    val menuViewModel: MenuViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()

    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()

    var selectedFoodItem by remember { mutableStateOf<FoodItem?>(null) }

    NavHost(
        navController = navController,
        startDestination = if (authState is AuthState.Authenticated) "menu" else "login"
    ) {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("menu") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("menu") {
            MenuScreen(
                menuViewModel = menuViewModel,
                cartViewModel = cartViewModel,
                onNavigateToCart = { navController.navigate("cart") },
                onNavigateToProfile = { navController.navigate("profile") },  // ADD THIS
                onFoodItemClick = { foodItem ->
                    selectedFoodItem = foodItem
                    navController.navigate("foodDetails")
                }
            )
        }

        composable("foodDetails") {
            selectedFoodItem?.let { foodItem ->
                FoodDetailsScreen(
                    foodItem = foodItem,
                    cartViewModel = cartViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable("cart") {
            CartScreen(
                cartViewModel = cartViewModel,
                onConfirmOrder = { navController.navigate("orderStatus") },
                onBack = { navController.popBackStack() },
                userId = authViewModel.currentUser?.uid ?: ""
            )
        }

        composable("orderStatus") {
            OrderStatusScreen(
                cartViewModel = cartViewModel,
                onBackToMenu = {
                    navController.popBackStack("menu", inclusive = false)
                    navController.navigate("menu")
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                authViewModel = authViewModel,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("menu") { inclusive = true }
                    }
                }
            )
        }
    }
}