package com.productivitybandits.focuspocusapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class DashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance()
        val userNameTextView = findViewById<TextView>(R.id.userNameTextView)
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val firstName = document.getString("firstName") ?: ""
                        val lastName = document.getString("lastName") ?: ""
                        userNameTextView.text = "$firstName $lastName"
                    } else {
                        userNameTextView.text = "Name not found"
                    }
                }
                .addOnFailureListener { e ->
                    userNameTextView.text = "Error loading name"
                    Log.e("Firestore", "Failed to fetch name: ${e.message}")
                }
        }


        // Nudges icon
        val nudgesIcon: ImageView = findViewById(R.id.nudgesIcon)
        nudgesIcon.setOnClickListener {
            openNudges(it)
        }

        // Tasks icon -> open TasksActivity
        val tasksIcon: LinearLayout = findViewById(R.id.tasksIcon)
        tasksIcon.setOnClickListener {
            openTasks(it)
        }

        // ☑️  Logout Button
        val logoutButton: Button = findViewById(R.id.accountLogout)
        logoutButton.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // ✅ Account Buttons
        findViewById<Button>(R.id.accountButton).setOnClickListener {
            Toast.makeText(this, "Account Clicked", Toast. LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.accountPreferences). setOnClickListener {
            Toast.makeText(this, "Preferences Clicked", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.accountCustomize).setOnClickListener {
            Toast.makeText(this, "Customize Clicked", Toast.LENGTH_SHORT).show()
        }
    }


    // 🧭 Opens Nudges Screen - Updating the Nudges Screen
    fun openNudges(view: View) {
        val intent = Intent(this, NudgesActivity::class.java)
        startActivity(intent)
    }

    // 🧭 Opens Tasks screen - Updating the task screen
    fun openTasks(view: View) {
        val intent = Intent(this, TasksActivity::class.java)
        startActivity(intent)
    }
}