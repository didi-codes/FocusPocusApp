package com.productivitybandits.focuspocusapp.ui.frames.home

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HomeApiClient {
    private const val BASE_URL = "https://api.example.com/" // Replace with real URL

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: HomeApiService by lazy {
        retrofit.create(HomeApiService::class.java)
    }
}
