package com.example.campusbite.models

data class CartItem(
    val foodItem: FoodItem,
    var quantity: Int = 1
) {
    val totalPrice: Double
        get() = foodItem.price * quantity
}