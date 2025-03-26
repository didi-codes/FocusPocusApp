package com.productivitybandits.focuspocusapp

import RetrofitClient
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.productivitybandits.focuspocusapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { _ ->
            val id = "dynamic_value" // Replace this with actual dynamic input logic
            fetchData(id)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun fetchData(id: String) {
        RetrofitClient.apiService.getData(id).enqueue(object : Callback<DataModel> {
            override fun onResponse(call: Call<DataModel>, response: Response<DataModel>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    Snackbar.make(binding.fab, "Fetched Data: ${data?.name}", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .show()
                } else {
                    Snackbar.make(binding.fab, "API Error: ${response.code()}", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .show()
                }
            }

            override fun onFailure(call: Call<DataModel>, t: Throwable) {
                Snackbar.make(binding.fab, "Network Error: ${t.message}", Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.fab)
                    .show()
            }
        })
    }
}