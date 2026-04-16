package com.example.campusbite.models

data class FoodItem(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val imageUrl: String = "",
    val category: String = "",
    val isAvailable: Boolean = true
)