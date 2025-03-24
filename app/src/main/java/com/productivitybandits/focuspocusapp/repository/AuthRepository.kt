package com.productivitybandits.focuspocusapp.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthRepository {

    // Using Stateflow for reactive authentication state
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated


    // Simulate Login (replace with actual API call or database query)
    fun login(username: String, password: String): Boolean {
        return if (username == "test" && password == "password") {
            _isAuthenticated.value = true
            true
        } else {
            false
        }

    }


    // Simulate Logout
    fun logout() {
        _isAuthenticated.value = false

    }
}