package com.productivitybandits.focuspocusapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.productivitybandits.taskmanager.T


class DashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance()

        fun openNudges() {
            val intent = Intent(this, NudgesActivity::class.java)
            startActivity(intent)
        }

        // Nudges icon
        val nudgesIcon: ImageView = findViewById(R.id.nudgesIcon)
        nudgesIcon.setOnClickListener {
            openNudges()
        }

        // Logout Functionality
        fun logoutUser() {
            auth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SplashActivity::class.java) // change this to your actual login screen
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        val logoutButton: Button = findViewById(R.id.accountLogout)
        logoutButton.setOnClickListener {
            logoutUser()
        }

        val accountButton: Button = findViewById(R.id.accountButton)
        accountButton.setOnClickListener {
            Toast.makeText(this, "Account Clicked", Toast.LENGTH_SHORT).show()
        }

        val preferencesButton: Button = findViewById(R.id.accountPreferences)
        preferencesButton.setOnClickListener {
            Toast.makeText(this, "Preferences Clicked", Toast.LENGTH_SHORT).show()
        }

        val customizeButton: Button = findViewById(R.id.accountCustomize)
        customizeButton.setOnClickListener {
            Toast.makeText(this, "Customize Clicked", Toast.LENGTH_SHORT).show()
        }


    }
}

