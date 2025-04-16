package com.productivitybandits.focuspocusapp.models
// Task Model duplicate
data class Task (
    val id: String,  // Unique task ID
    val title: String,  // Task name
    val time: String,  // Task time
    val isCompleted: Boolean  // Completion state
)