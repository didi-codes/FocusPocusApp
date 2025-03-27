package com.productivitybandits.focuspocusapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sampleId = "12345"  // Example dynamic value
        sampleId.fetchData()
    }

    private fun String.fetchData() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.getData(this@fetchData).execute()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        data?.let {
                            showSnackbar("Fetched Data: ${it.name}")
                        } ?: showSnackbar("Error: No data received")
                    } else {
                        showSnackbar("API Error: ${response.code()}")
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) { showSnackbar("Network Error: ${e.message}") }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) { showSnackbar("Server Error: ${e.message}") }
            }
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }
}
