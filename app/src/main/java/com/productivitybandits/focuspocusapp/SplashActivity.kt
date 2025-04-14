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
import com.google.firebase.FirebaseApp
import com.productivitybandits.user_authentication.loginUser
import com.productivitybandits.user_authentication.registerUser

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private val users = mutableMapOf<String, String>() // Temporary storage for user credentials

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

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
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            isSingleLine = true
        }

        layout.addView(usernameInput)
        layout.addView(passwordInput)


        MaterialAlertDialogBuilder(this)
            .setTitle("Login Required")
            .setView(layout)
            .setPositiveButton("Login") { _, _ ->
                val username = usernameInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()

                if (username != "" && password != "") {
                    loginUser(username, password) {
                            showToast("Login successful! Navigating to Dashboard...")
                            val intent = Intent(this, DashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                    }
                } else {
                    showToast("Please input email and password to login")
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
                        registerUser(email, password, username, "", "") { success, message ->
                            if (success) {
                                // On successful registration, store the username and email
                                val sessionManager = SessionManager(this)
                                sessionManager.saveUser(username, password) // Save user data
                                sessionManager.saveEmail(email) // Save email
                                showToast("Sign-up successful! Navigating to Dashboard...")
                                val intent = Intent(this, DashboardActivity::class.java)
                                startActivity(intent)
                                finish()  // Close the SplashActivity
                            } else {
                                showToast("Sign-up failed: $message")
                            }
                        }
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
