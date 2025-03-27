package com.productivitybandits.focuspocusapp

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.widget.Toast
import com.productivitybandits.focuspocusapp.utils.SessionManager
import android.util.Log

class SplashActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private val users = mutableMapOf<String, String>() // Temporary storage for user credentials

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
            Log.d("SplashDebug", "Enter button clicked")
            if (sessionManager.getLoginState()) {
                Log.d("SplashDebug", "User already logged in, navigating to Dashboard")
                navigateToDashboard()
            } else {
                Log.d("SplashDebug", "User not logged in, showing login dialog")
                showLoginDialog()
            }
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoginDialog() {
        Log.d("SplashDebug", "Login dialog show not show")
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 40, 60, 0)
        }

        val usernameInput = EditText(this).apply {
            hint = "Username"
            inputType = android.text.InputType.TYPE_CLASS_TEXT
            isSingleLine = true
        }

        val passwordInput = EditText(this).apply {
            hint = "Password"
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            isSingleLine = true
        }

        layout.addView(usernameInput)
        layout.addView(passwordInput)

        val sessionManager = SessionManager(this)

        MaterialAlertDialogBuilder(this)
            .setTitle("Login Required")
            .setView(layout)
            .setPositiveButton("Login") { _, _ ->
                val username = usernameInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()

                if (username.isNotEmpty() && password.isNotEmpty() &&
                    sessionManager.validateUser(username, password)) {
                    sessionManager.saveLoginState(true)
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                } else {
                    showErrorDialog()
                }
            }
            .setNegativeButton("Cancel", null)
            .setNeutralButton("Sign Up") { _, _ ->
                showSignUpDialog()
            }
            .setCancelable(false)
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
            inputType = android.text.InputType.TYPE_CLASS_TEXT
            isSingleLine = true
        }

        val emailInput = EditText(this).apply {
            hint = "Email Address"
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            isSingleLine = true
        }

        val passwordInput = EditText(this).apply {
            hint = "Password"
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            isSingleLine = true
        }

        val confirmPasswordInput = EditText(this).apply {
            hint = "Confirm Password"
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            isSingleLine = true
        }

        layout.addView(usernameInput)
        layout.addView(emailInput)
        layout.addView(passwordInput)
        layout.addView(confirmPasswordInput)

        val sessionManager = SessionManager(this)

        MaterialAlertDialogBuilder(this)
            .setTitle("Sign Up")
            .setView(layout)
            .setPositiveButton("Create Account") { _, _ ->
                val username = usernameInput.text.toString().trim()
                val email = emailInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()
                val confirmPassword = confirmPasswordInput.text.toString().trim()

                when {
                    username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ->
                        showToast("All fields are required!")

                    password != confirmPassword ->
                        showToast("Passwords do not match!")

                    users.containsKey(username) ->
                        showToast("Username already exists!")

                    else -> {
                        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            showToast("Enter a valid email address!")
                            return@setPositiveButton
                        }

                        sessionManager.saveUser(username, password)// Store new user
                        sessionManager.saveEmail(email) // Saves email
                        showToast("Sign-up successful! Please log in.")
                        showLoginDialog()
                    }
                }
            }
            .setNeutralButton("Cancel", null)
            .setCancelable(false)
            .create()
            .show()
    }

    private fun showErrorDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Login Failed")
            .setMessage("Incorrect username or password. Please try again.")
            .setPositiveButton("OK") { _, _ -> showLoginDialog() }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
