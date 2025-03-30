package com.productivitybandits.focuspocusapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NudgesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nudges)

        findViewById<TextView>(R.id.tab_home).setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        findViewById<TextView>(R.id.tab_tasks).setOnClickListener {
            //startActivity(Intent(this, TasksActivity::class.java))
        }

        findViewById<TextView>(R.id.tab_routine).setOnClickListener {
          //  startActivity(Intent(this, RoutineActivity::class.java))
        }

        findViewById<TextView>(R.id.tab_nudges).setOnClickListener {
            startActivity(Intent(this, NudgesActivity::class.java))
        }

        findViewById<TextView>(R.id.tab_insights).setOnClickListener {
          //  startActivity(Intent(this, InsightsActivity::class.java))

        }

        findViewById<TextView>(R.id.tab_selfcare).setOnClickListener {
         //   startActivity(Intent(this, SelfCareActivity::class.java))


        }

    }
}
