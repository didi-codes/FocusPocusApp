package com.productivitybandits.focuspocusapp

import com.productivitybandits.focuspocusapp.models.LoginRequest
import com.productivitybandits.focuspocusapp.models.SignUpRequest
import com.productivitybandits.focuspocusapp.models.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val userId: String)

data class SignUpRequest(val name: String, val email: String, val password: String)
data class SignUpResponse(val success: Boolean, val message: String)

interface ApiService {

    // 🔐 LOGIN ENDPOINT
    // Sends a POST request to /auth/login with username & password in the body.
    // Backend should return a success/failure response.
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<Void>

    // ✨ SIGNUP ENDPOINT
    // Sends a POST request to /auth/signup with username, email, and password.
    // Backend handles creating a new user account.
    @POST("auth/signup")
    suspend fun signUpUser(@Body request: SignUpRequest): Response<Void>
}

