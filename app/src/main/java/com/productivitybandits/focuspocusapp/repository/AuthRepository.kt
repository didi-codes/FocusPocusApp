package com.productivitybandits.focuspocusapp.repository

import com.productivitybandits.focuspocusapp.RetrofitClient
import com.productivitybandits.focuspocusapp.models.LoginRequest
import com.productivitybandits.focuspocusapp.models.SignUpRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthRepository {

    // 🔄 Used to track login state within the app
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    // 🔐 Login Function
    // Calls the login API endpoint with username & password.
    // Backend should return a 2xx response on success.
    suspend fun login(username: String, password: String): Boolean {
        return try {
            val response = RetrofitClient.apiService.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                _isAuthenticated.value = true  // Update local auth state
                true
            } else {
                false  // Login failed – possibly invalid credentials
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false // Network error or server unreachable
        }
    }

    // ✨ Sign Up Function
    // Calls the signup API endpoint with username, email, and password.
    // Backend should handle user creation and return success/failure.
    suspend fun signUp(username: String, email: String, password: String): Boolean {
        return try {
            val request = SignUpRequest(username, email, password)
            val response = RetrofitClient.apiService.signUpUser(request)
            response.isSuccessful  // true if 2xx, false otherwise
        } catch (e: Exception) {
            e.printStackTrace()
            false // Handles failure like network timeout or exception
        }
    }

    // 🔓 Logout Function
    // Resets local auth state (no network call).
    suspend fun logout() {
        _isAuthenticated.value = false

    }
}