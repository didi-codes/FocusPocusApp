package com.productivitybandits.focuspocusapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.productivitybandits.focuspocusapp.adapters.CalendarEventAdapter
import com.productivitybandits.focuspocusapp.adapters.TaskAdapter
import com.productivitybandits.focuspocusapp.calendar.CalendarEvent
import com.productivitybandits.focuspocusapp.calendar.CalendarHelper
import com.productivitybandits.focuspocusapp.calendar.CalendarHelper.fetchCalendarEvents
import com.productivitybandits.focuspocusapp.repository.TasksRepository
import com.productivitybandits.focuspocusapp.viewmodel.TasksViewModel
import com.productivitybandits.focuspocusapp.viewmodel.TasksViewModelFactory
import kotlinx.coroutines.launch
import java.util.Date

class TasksActivity : AppCompatActivity() {

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    // 🧠 Connects TasksViewModel to Repository (for API communication)
    private val tasksViewModel: TasksViewModel by viewModels{
        TasksViewModelFactory(TasksRepository())
    }

    private lateinit var taskAdapter: TaskAdapter  // Adapter for RecyclerView
    private lateinit var calendarAdapter: CalendarEventAdapter

    fun logCalendarAccounts(context: Context) {
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        )

        val cursor = context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(0)
                val accountName = it.getString(1)
                val displayName = it.getString(2)
                Log.d("CalendarInfo", "ID: $id | Account: $accountName | Display: $displayName")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        logCalendarAccounts(this)

        val calendarAccountTextView = findViewById<TextView>(R.id.calendarAccountInfoTextView)
        val events = fetchCalendarEvents(this)
        val eventText = events.joinToString(separator = "\n") { event ->
            "• ${event.title} (${Date(event.startTimeMillis)})"
        }
        calendarAccountTextView.text = eventText
        
        val calendarRecycler = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        calendarAdapter = CalendarEventAdapter(emptyList(), ::onConfirmEvent, ::onDismissEvent)
        calendarRecycler.layoutManager = LinearLayoutManager(this)
        calendarRecycler.adapter = calendarAdapter

        // 🔄 Initialize RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.tasksRecyclerView)
        taskAdapter = TaskAdapter(emptyList()) // starts with empty list
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        // 🚀 Fetch tasks from backend and observe changes
        lifecycleScope.launch {
            if (userId != null) {
                Log.d("Debug", "Current UID: $userId")
                tasksViewModel.fetchTasks(userId) // API call to get tasks
            } else {
                Log.e("Auth", "User is not authenticated, uid is null")
            }

            tasksViewModel.tasks.collect { tasksList ->
                taskAdapter.updateData(tasksList) // update adapter with new data
                tasksList.forEach {
                    Log.d("TasksActivity", "Fetched Task: ${it.title}")
                }
            }
        }

        // Check permission to read the calendar and request it if needed
        if (checkSelfPermission(android.Manifest.permission.READ_CALENDAR) !=
            android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_CALENDAR), 100)
        } else {
            // Permission granted, fetch and log calendar events
            fetchAndLogCalendarEvents()
        }

        //  ✅ Handle Confirm and Dismiss Button Logic
        // For now, acts on the first task in the list - can be customized later
        val confirmButton = findViewById<Button>(R.id.btn_confirm)
        val dismissButton = findViewById<Button>(R.id.btn_dismiss)

        confirmButton.setOnClickListener {
            val firstTask = tasksViewModel.tasks.value.firstOrNull()
            firstTask?.let {
                tasksViewModel.confirmTask(userId!!, it.id)// Marks task as completed via API
            }
        }

        dismissButton.setOnClickListener {
            val firstTask = tasksViewModel.tasks.value.firstOrNull()
            firstTask?.let {
                tasksViewModel.deleteTask(userId!!, it.id) // Deletes task via API
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

    private fun fetchAndLogCalendarEvents() {
        val events = CalendarHelper.fetchCalendarEvents(this)
        events.forEach {
            Log.d("CalendarEvent", "Event: ${it.title}, Start: ${Date(it.startTimeMillis)}")
        }
    }

    private fun onConfirmEvent(event: CalendarEvent) {
        Log.d("TasksActivity", "Event Confirmed: ${event.title}")
        // Handle confirmation logic here (e.g., mark as confirmed in DB)
    }

    private fun onDismissEvent(event: CalendarEvent) {
        Log.d("TasksActivity", "Event Dismissed: ${event.title}")
        // Handle dismiss logic here (e.g., delete event or mark as dismissed)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with fetching calendar events
                fetchAndLogCalendarEvents()
            } else {
                // Permission denied, you may want to notify the user
                Log.e("Permissions", "Calendar permission denied")
                // Optionally show a message to explain that this permission is required
            }
        }
    }
}