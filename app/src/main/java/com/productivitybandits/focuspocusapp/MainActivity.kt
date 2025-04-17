package com.productivitybandits.focuspocusapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example trigger (replace with a button click or real logic)
        fetchData()
    }

    private fun fetchData() {
        val staticId = "12345" // static ID used for the API call

        RetrofitClient.apiService.getData(staticId).enqueue(object : Callback<DataModel> {
            override fun onResponse(call: Call<DataModel>, response: Response<DataModel>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Fetched Data: ${data?.name}",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "API Error: ${response.code()}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<DataModel>, t: Throwable) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Network Error: ${t.message}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }
}
