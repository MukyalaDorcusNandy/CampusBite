package com.example.campusbite.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusbite.models.CartItem
import com.example.campusbite.models.FoodItem
import com.example.campusbite.models.Order
import com.example.campusbite.models.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _currentOrder = MutableStateFlow<Order?>(null)
    val currentOrder: StateFlow<Order?> = _currentOrder

    private val _orderStatus = MutableStateFlow<OrderStatus?>(null)
    val orderStatus: StateFlow<OrderStatus?> = _orderStatus

    fun addToCart(foodItem: FoodItem) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.foodItem.id == foodItem.id }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            currentItems.add(CartItem(foodItem))
        }
        _cartItems.value = currentItems
    }

    fun removeFromCart(cartItem: CartItem) {
        _cartItems.value = _cartItems.value.filter { it.foodItem.id != cartItem.foodItem.id }
    }

    fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(cartItem)
            return
        }

        val currentItems = _cartItems.value.toMutableList()
        val index = currentItems.indexOfFirst { it.foodItem.id == cartItem.foodItem.id }
        if (index != -1) {
            currentItems[index].quantity = newQuantity
            _cartItems.value = currentItems
        }
    }

    fun getTotalPrice(): Double {
        return _cartItems.value.sumOf { it.totalPrice }
    }

    fun getItemCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun placeOrder(userId: String, onSuccess: () -> Unit) {
        if (_cartItems.value.isEmpty()) return

        val order = Order(
            userId = userId,
            items = _cartItems.value,
            totalAmount = getTotalPrice(),
            status = OrderStatus.PENDING,
            estimatedPickupTime = "15-20 minutes"
        )

        _currentOrder.value = order
        _orderStatus.value = OrderStatus.PENDING

        viewModelScope.launch {
            kotlinx.coroutines.delay(2000)
            _orderStatus.value = OrderStatus.CONFIRMED

            kotlinx.coroutines.delay(3000)
            _orderStatus.value = OrderStatus.PREPARING

            kotlinx.coroutines.delay(5000)
            _orderStatus.value = OrderStatus.READY_FOR_PICKUP
        }

        clearCart()
        onSuccess()
    }

    fun resetOrder() {
        _currentOrder.value = null
        _orderStatus.value = null
    }
}