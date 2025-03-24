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


class SplashActivity : AppCompatActivity() {
    private val users = mutableMapOf("test" to "password") // Simulated database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags (
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_splash)

        val enterButton: Button = findViewById(R.id.btnEnter)

        // To delay the transition to MainActivity
        enterButton.setOnClickListener {
            showLoginDialog()
        }
    }

    private fun showLoginDialog() {
        val layout = LinearLayout(this). apply {
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
            inputType= android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            isSingleLine= true
        }

        layout.addView(usernameInput)
        layout.addView(passwordInput)

        MaterialAlertDialogBuilder(this)
            .setTitle("Login Required")
            .setView(layout)
            .setPositiveButton("Login") { _, _ ->
                val username = usernameInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()

                if (users[username] == password) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
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

    private fun showSignUpDialog(){
        val layout = LinearLayout(this). apply {
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
            inputType = android.text.InputType. TYPE_TEXT_VARIATION_PASSWORD
            isSingleLine = true
        }

        val confirmPasswordInput = EditText(this).apply {
            hint = "Confirm Password"
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            isSingleLine = true
        }

        layout.addView(usernameInput)
        layout.addView(passwordInput)
        layout.addView(confirmPasswordInput)

        MaterialAlertDialogBuilder(this)
            .setTitle("Sign Up")
            .setView(layout)
            .setPositiveButton("Create Account") { _, _ ->
                val username = usernameInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()
                val confirmPassword = confirmPasswordInput.text.toString().trim()

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    showToast("All fields are required!")
                } else if (password != confirmPassword) {
                    showToast("Passwords do not match!")
                } else if (users.containsKey(username)) {
                    showToast("Username already exists!")
                } else {
                    users[username] = password // Store new user
                    showToast("Sign-up successful! Please log in.")
                    showLoginDialog()
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
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}