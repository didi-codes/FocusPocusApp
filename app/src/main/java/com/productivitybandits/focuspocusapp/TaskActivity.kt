package com.productivitybandits.focuspocusapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.productivitybandits.focuspocusapp.adapters.TaskAdapter
import com.productivitybandits.focuspocusapp.repository.TasksRepository
import com.productivitybandits.focuspocusapp.viewmodel.TasksViewModel
import com.productivitybandits.focuspocusapp.viewmodel.TasksViewModelFactory
import kotlinx.coroutines.launch

class TasksActivity : AppCompatActivity() {

    // 🧠 Connects TasksViewModel to Repository (for API communication)
    private val tasksViewModel: TasksViewModel by viewModels{
        TasksViewModelFactory(TasksRepository())
    }

    private lateinit var taskAdapter: TaskAdapter  // Adapter for RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        // 🔄 Initialize RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.tasksRecyclerView)
        taskAdapter = TaskAdapter(emptyList()) // starts with empty list
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        // 🚀 Fetch tasks from backend and observe changes
        lifecycleScope.launch {
            tasksViewModel.fetchTasks() // API call to get tasks

            tasksViewModel.tasks.collect { tasksList ->
                taskAdapter.updateData(tasksList) // update adapter with new data
                tasksList.forEach {
                    Log.d("TasksActivity", "Fetched Task: ${it.title}")
                }
            }
        }

        //  ✅ Handle Confirm and Dismiss Button Logic
        // For now, acts on the first task in the list - can be customized later
        val confirmButton = findViewById<Button>(R.id.btn_confirm)
        val dismissButton = findViewById<Button>(R.id.btn_dismiss)

        confirmButton.setOnClickListener {
            val firstTask = tasksViewModel.tasks.value.firstOrNull()
            firstTask?.let {
                tasksViewModel.completeTask(it.id) // Marks task as completed via API
            }
        }

        dismissButton.setOnClickListener {
            val firstTask = tasksViewModel.tasks.value.firstOrNull()
            firstTask?.let {
                tasksViewModel.deleteTask(it.id) // Deletes task via API
            }
        }

        // 🔄 Bottom Navigation Actions
        findViewById<TextView>(R.id.tab_home).setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        findViewById<TextView>(R.id.tab_nudges).setOnClickListener {
            startActivity(Intent(this, NudgesActivity::class.java))
        }

        findViewById<TextView>(R.id.tab_tasks).setOnClickListener {

        }
    }
}