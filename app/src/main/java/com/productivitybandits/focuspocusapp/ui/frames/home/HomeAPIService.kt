package com.productivitybandits.focuspocusapp.ui.frames.home

import retrofit2.Call
import retrofit2.http.GET

interface HomeApiService {
    @GET("home") // Replace with your actual endpoint
    fun getHomeData(): Call<HomeDataModel>
}
