package com.productivitybandits.focuspocusapp

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.productivitybandits.focuspocusapp.repository.AuthRepository
import com.productivitybandits.focuspocusapp.utils.SessionManager
import com.productivitybandits.focuspocusapp.viewmodel.AuthViewModel
import com.productivitybandits.focuspocusapp.AuthViewModelFactory
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    // Calls login/signup API and handles response – will display toast and navigate accordingly
    private val authViewModel:AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepository(), SessionManager(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_splash)

        sessionManager = SessionManager(this)
        sessionManager.clearSession()

        val enterButton: Button = findViewById(R.id.btnEnter)

        enterButton.setOnClickListener {
            if (sessionManager.getLoginState()) {
                navigateToDashboard()
            } else {
                showLoginDialog()
            }
        }
    }

    private fun navigateToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    private fun showLoginDialog() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 40, 60, 0)
        }

        val usernameInput = EditText(this).apply {
            hint = "Username"
        }

        val passwordInput = EditText(this).apply {
            hint = "Password"
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        layout.addView(usernameInput)
        layout.addView(passwordInput)


        MaterialAlertDialogBuilder(this)
            .setTitle("Login Required")
            .setView(layout)
            .setPositiveButton("Login") { _, _ ->
                val username = usernameInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()

                lifecycleScope.launch {
                    // Calls login/signup API and handles response – will display toast and navigate accordingly
                    val success = authViewModel.login(username, password)
                    if (success) {
                        showToast("Login successful")
                        navigateToDashboard()
                    } else {
                        showToast("Login failed. Try again.")
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .setNeutralButton("Sign Up") { _, _ ->
                showSignUpDialog()
            }
            .create()
            .show()
    }

    private fun showSignUpDialog() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 40, 60, 0)
        }

        val usernameInput = EditText(this).apply {
            hint = "Username"
        }

        val emailInput = EditText(this).apply {
            hint = "Email Address"
        }

        val passwordInput = EditText(this).apply {
            hint = "Password"
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        val confirmPasswordInput = EditText(this).apply {
            hint = "Confirm Password"
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        layout.addView(usernameInput)
        layout.addView(emailInput)
        layout.addView(passwordInput)
        layout.addView(confirmPasswordInput)


        MaterialAlertDialogBuilder(this)
            .setTitle("Sign Up")
            .setView(layout)
            .setPositiveButton("Create Account") { _, _ ->
                val username = usernameInput.text.toString().trim()
                val email = emailInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()
                val confirmPassword = confirmPasswordInput.text.toString().trim()

                if (
                    username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    showToast("All fields are required!")
                    return@setPositiveButton
                }
                if (password != confirmPassword) {
                    showToast("Passwords do not match!")
                    return@setPositiveButton
                }

                lifecycleScope.launch {
                    // Calls login/signup API and handles response
                    // – will display toast and navigate accordingly
                    val success = authViewModel.signUp(username, email, password)
                    if (success) {
                        showToast("Sign-up successful. Please log in.")
                        showLoginDialog()
                    } else {
                        showToast("Sign-up failed.")
                    }
                }
            }
            .setNeutralButton("Cancel", null)
            .setCancelable(false)
            .create()
            .show()
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
