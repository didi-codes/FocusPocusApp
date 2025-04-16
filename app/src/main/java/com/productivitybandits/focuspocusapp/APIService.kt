package com.productivitybandits.focuspocusapp

import com.productivitybandits.focuspocusapp.models.LoginRequest
import com.productivitybandits.focuspocusapp.models.SignUpRequest
import com.productivitybandits.focuspocusapp.models.SignUpResponse
import com.productivitybandits.focuspocusapp.models.*
import com.productivitybandits.focuspocusapp.models.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.*
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path

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

    // 📥 Fetch all nudges
    @GET("nudges")
    suspend fun getNudges(): Response<List<Nudge>>

    // ➕ Add a new nudge
    @POST("nudges")
    suspend fun addNudge(@Body nudge: Nudge): Response<Nudge>

    // ❌ Delete a nudge by ID
    @DELETE("nudges/{id}")
    suspend fun deleteNudge(@Path("id") id: String)

    // ✅ Mark a nudge as completed
    @PUT("nudges/{id}/complete")
    suspend fun completeNudge(@Path("id") id: String): Response<Nudge>

    // 📥 Fetch all tasks
    @GET("tasks")
    suspend fun getTasks(): Response<List<Task>>

    // ➕ Add a new task
    @POST("tasks")
    suspend fun addTask(@Body task: Task): Response<Task>

    // ❌ Delete a task
    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String)

    // ✅ Mark a task as completed
    @PUT("tasks/{id}/complete")
    suspend fun completeTask(@Path("id") id: String): Response<Task>
}

