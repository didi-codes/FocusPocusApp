package com.productivitybandits.focuspocusapp.repository

import com.productivitybandits.focuspocusapp.RetrofitClient
import com.productivitybandits.focuspocusapp.models.LoginRequest
import com.productivitybandits.focuspocusapp.models.SignUpRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthRepository {


    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    // 🔐 Login Function
    suspend fun login(username: String, password: String): Boolean {
        return try {
            val response = RetrofitClient.apiService.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                _isAuthenticated.value = true
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // ✨ Sign Up Function
    suspend fun signUp(username: String, email: String, password: String): Boolean {
        return try {
            val request = SignUpRequest(username, email, password)
            val response = RetrofitClient.apiService.signUpUser(request)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // 🔓 Logout Function
    suspend fun logout() {
        _isAuthenticated.value = false

    }
}