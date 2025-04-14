package com.productivitybandits.focuspocusapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.productivitybandits.focuspocusapp.adapters.NudgeAdapter
import com.productivitybandits.focuspocusapp.repository.NudgesRepository
import com.productivitybandits.focuspocusapp.viewmodel.NudgesViewModel
import com.productivitybandits.focuspocusapp.viewmodel.NudgesViewModelFactory
import kotlinx.coroutines.launch

class NudgesActivity : AppCompatActivity() {

    // 🔌 viewModel connection to NudgesRepository for API access
    private val nudgesViewModel: NudgesViewModel by viewModels {
        NudgesViewModelFactory(NudgesRepository())

    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var nudgeAdapter: NudgeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nudges)

        // 🔁 Setup RecyclerView and Adapter
        recyclerView = findViewById(R.id.nudgesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        nudgeAdapter = NudgeAdapter(emptyList())
        recyclerView.adapter = nudgeAdapter




        // 🧠 Fetch nudges from the backend (via API)
        lifecycleScope.launch {
            nudgesViewModel.fetchNudges()
            nudgesViewModel.nudges.collect { nudgesList ->
                Log.d("NudgesActivity", "Fetched ${nudgesList.size} nudges")
                nudgeAdapter.updateData(nudgesList)
            }
        }

        // ✅ Bottom navigation setup
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
