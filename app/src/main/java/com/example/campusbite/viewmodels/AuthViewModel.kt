package com.example.campusbite.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    val currentUser: SimulatedUser?
        get() = if (_authState.value is AuthState.Authenticated) {
            SimulatedUser("user123", "student@university.edu")
        } else null

    fun login(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        if (email.isNotBlank() && password.isNotBlank()) {
            _authState.value = AuthState.Authenticated(SimulatedUser("user123", email))
            onResult(true, "Login successful")
        } else {
            onResult(false, "Please enter email and password")
        }
    }

    fun register(name: String, email: String, password: String, onResult: (Boolean, String) -> Unit) {
        if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
            _authState.value = AuthState.Authenticated(SimulatedUser("user123", email))
            onResult(true, "Registration successful")
        } else {
            onResult(false, "Please fill all fields")
        }
    }

    fun logout() {
        _authState.value = AuthState.Unauthenticated
    }
}

data class SimulatedUser(
    val uid: String,
    val email: String
)

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Authenticated(val user: SimulatedUser) : AuthState()
}