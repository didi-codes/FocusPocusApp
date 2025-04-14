package com.productivitybandits.focuspocusapp.repository

import com.productivitybandits.focuspocusapp.RetrofitClient
import com.productivitybandits.focuspocusapp.models.Nudge
import retrofit2.HttpException


// 🔄 Handles API interactions for Nudges using RetrofitClient.apiService
class NudgesRepository {

    // 📥 Get list of all nudges from API
    suspend fun getNudges(): List<Nudge> {
        val response = RetrofitClient.apiService.getNudges()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw HttpException(response)
        }
    }

    // ➕ Add a new nudge to API
    suspend fun addNudge(nudge: Nudge): Nudge {
        val response = RetrofitClient.apiService.addNudge(nudge)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw HttpException(response)
        }
    }

    // ❌ Delete a nudge by ID
    suspend fun deleteNudge(id: String) {
        RetrofitClient.apiService.deleteNudge(id) // No response expected
    }

    // ✅ Mark a nudge as completed
    suspend fun markNudgeCompleted(id: String): Nudge {     // Backend should return updated Nudge after cpmpletion
        val response = RetrofitClient.apiService.completeNudge(id)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw HttpException(response)
        }
    }
}