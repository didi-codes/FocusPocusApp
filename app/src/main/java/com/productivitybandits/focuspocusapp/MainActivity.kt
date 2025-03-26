package com.productivitybandits.focuspocusapp

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.productivitybandits.focuspocusapp.databinding.ActivityMainBinding
import com.productivitybandits.focuspocusapp.viewmodel.AuthViewModel
import com.productivitybandits.focuspocusapp.AuthViewModelFactory
import com.productivitybandits.focuspocusapp.repository.AuthRepository
import com.productivitybandits.focuspocusapp.utils.SessionManager
import kotlinx.coroutines.launch
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.repeatOnLifecycle
import android.content.Intent



class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }

        val navView: BottomNavigationView = binding.navView
        navView.setupWithNavController(navController)

        val sessionManager = SessionManager(this)
        val authRepository = AuthRepository()
        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(authRepository, sessionManager)
        )[AuthViewModel::class.java]

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    // Show Login Dialog when not authenticated
    private fun showLoginDialog() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 40, 60, 0)
        }

        val usernameInput = EditText(this).apply {
            hint = "username"
            inputType = android.text.InputType.TYPE_CLASS_TEXT
            isSingleLine = true
            background = getDrawable(R.drawable.input_background)
            setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_user,
                0,
                0,
                0
            )
            compoundDrawablePadding = 16
        }

        val passwordInput = EditText(this).apply {
            hint = "Password"
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            isSingleLine = true
            background = getDrawable(R.drawable.input_background)
            setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_lock,
                0,
                0,
                0
            )
            compoundDrawablePadding = 16
        }

        layout.addView(usernameInput)
        layout.addView(passwordInput)

        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Login Required")
            .setView(layout)
            .setPositiveButton("Login") { _, _ ->
                val username = usernameInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()

                lifecycleScope.launch {
                    val success = authViewModel.login(username, password)
                    if (success) {
                        val intent = Intent(this@MainActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        showLoginError()
                    }
                }
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialog.show()
    }

    private fun showLoginError() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Login Failed")
            .setMessage("Incorrect username or password. Please try again.")
            .setPositiveButton("OK") { _, _ ->
                showLoginDialog()
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
