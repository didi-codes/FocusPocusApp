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
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<Void>

    // ✨ SIGNUP ENDPOINT
    @POST("auth/signup")
    suspend fun signUpUser(@Body request: SignUpRequest): Response<Void>
}