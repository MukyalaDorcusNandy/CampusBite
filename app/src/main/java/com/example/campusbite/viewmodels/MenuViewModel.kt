package com.example.campusbite.viewmodels

import androidx.lifecycle.ViewModel
import com.example.campusbite.models.FoodItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MenuViewModel : ViewModel() {
    private val _menuItems = MutableStateFlow<List<FoodItem>>(emptyList())
    val menuItems: StateFlow<List<FoodItem>> = _menuItems

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory

    init {
        loadSampleMenu()
    }

    private fun loadSampleMenu() {
        val sampleItems = listOf(
            FoodItem("1", "Burger", 5.99, "Juicy beef burger with lettuce and tomato", "", "Main", true),
            FoodItem("2", "Pizza Slice", 3.99, "Cheese pizza with your choice of toppings", "", "Main", true),
            FoodItem("3", "Caesar Salad", 4.99, "Fresh romaine lettuce with Caesar dressing", "", "Salads", true),
            FoodItem("4", "French Fries", 2.49, "Crispy golden fries", "", "Sides", true),
            FoodItem("5", "Chicken Sandwich", 6.49, "Grilled chicken with mayo and lettuce", "", "Main", true),
            FoodItem("6", "Soda", 1.99, "Regular soda", "", "Drinks", true),
            FoodItem("7", "Coffee", 2.49, "Fresh brewed coffee", "", "Drinks", true),
            FoodItem("8", "Chocolate Chip Cookie", 1.49, "Fresh baked cookie", "", "Desserts", true)
        )
        _menuItems.value = sampleItems
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun getFilteredItems(): List<FoodItem> {
        return if (_selectedCategory.value == "All") {
            _menuItems.value
        } else {
            _menuItems.value.filter { it.category == _selectedCategory.value }
        }
    }

    fun getCategories(): List<String> {
        val categories = _menuItems.value.map { it.category }.distinct().toMutableList()
        categories.add(0, "All")
        return categories
    }
}