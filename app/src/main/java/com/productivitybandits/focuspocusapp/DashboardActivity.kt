package com.productivitybandits.focuspocusapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity


class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

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
    }


    // 🧭 Opens Nudges Screen
    fun openNudges(view: View) {
        val intent = Intent(this, NudgesActivity::class.java)
        startActivity(intent)
    }

    // 🧭 Opens Tasks screen
    fun openTasks(view: View) {
        val intent = Intent(this, TasksActivity::class.java)
        startActivity(intent)
    }
}