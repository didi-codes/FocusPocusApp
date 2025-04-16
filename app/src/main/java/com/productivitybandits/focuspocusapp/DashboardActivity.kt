package com.productivitybandits.focuspocusapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Nudges icon
        val nudgesIcon: ImageView = findViewById(R.id.nudgesIcon)
        }

    fun openNudges(view: View) {
        val intent = Intent(this, NudgesActivity::class.java)
        startActivity(intent)
    }
}