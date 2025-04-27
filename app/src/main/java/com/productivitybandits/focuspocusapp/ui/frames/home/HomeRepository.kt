package com.productivitybandits.focuspocusapp.ui.frames.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object HomeRepository {
    private val homeData = MutableLiveData<HomeDataModel>()

    fun fetchHomeData(): LiveData<HomeDataModel> {
        HomeApiClient.apiService.getHomeData().enqueue(object : Callback<HomeDataModel> {
            override fun onResponse(call: Call<HomeDataModel>, response: Response<HomeDataModel>) {
                if (response.isSuccessful) {
                    homeData.value = response.body()
                }
            }

            override fun onFailure(call: Call<HomeDataModel>, t: Throwable) {
                // Optionally log or handle error
            }
        })
        return homeData
    }
}
