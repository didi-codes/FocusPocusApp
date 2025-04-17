package com.productivitybandits.focuspocusapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseApp
import com.productivitybandits.focuspocusapp.repository.AuthRepository
import com.productivitybandits.focuspocusapp.utils.SessionManager
import com.productivitybandits.focuspocusapp.viewmodel.AuthViewModel
import com.productivitybandits.focuspocusapp.viewmodel.AuthViewModelFactory
import com.productivitybandits.user_authentication.loginUser
import com.productivitybandits.user_authentication.registerUser

class SplashActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    // Calls login/signup API and handles response – will display toast and navigate accordingly
    private val authViewModel:AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepository(), SessionManager(this))
    }

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

                if (username.isNotBlank() && password.isNotBlank()) {
                    loginUser(username, password) {
                        showToast("Login successful! Navigating to Dashboard...")
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    showToast("Please Enter Both Username and Password")
                }
            }
            .setNegativeButton("Cancel", null)
            .setNeutralButton("Sign Up") { _, _ ->
                showSignUpDialog()
            }
            .create()
            .show()
    }
    private fun setupPasswordToggle(editText: EditText, context: Context) {
        var isPasswordVisible = false
        val eyeOpen = ContextCompat.getDrawable(context, R.drawable.visibility)
        val eyeClosed = ContextCompat.getDrawable(context, R.drawable.visibility_off)

        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, eyeClosed, null)
        editText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2
                val drawable = editText.compoundDrawables[drawableEnd]
                if (drawable != null && event.rawX >= (editText.right - drawable.bounds.width())) {
                    isPasswordVisible = !isPasswordVisible
                    editText.inputType = if (isPasswordVisible)
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    else
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    editText.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        if (isPasswordVisible) eyeOpen else eyeClosed,
                        null
                    )

                    // Keep cursor at end
                    editText.setSelection(editText.text.length)
                    return@setOnTouchListener true
                }
            }
            false
        }
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
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        val confirmPasswordInput = EditText(this).apply {
            hint = "Confirm Password"
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        setupPasswordToggle(passwordInput, this)
        setupPasswordToggle(confirmPasswordInput, this)

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
                    username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                        showToast("All fields are required!")
                        return@setPositiveButton
                    }

                    password != confirmPassword -> {
                        showToast("Passwords do not match!")
                        return@setPositiveButton
                    }

                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        showToast("Enter a valid email address!")
                        return@setPositiveButton
                    }

                    else -> {
                        registerUser(email, password, username, "", "") { success, message ->
                            if (success) {
                                val sessionManager = SessionManager(this)
                                sessionManager.saveUser(username, password)
                                sessionManager.saveEmail(email)
                                showToast("Sign-up successful! Navigating to Dashboard...")
                                val intent = Intent(this, DashboardActivity::class.java)
                                startActivity(intent)
                                finish()
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



    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}