package com.productivitybandits.taskmanager

import com.google.type.Date

data class Task<Time>(
    val title: String="",
    val description: String="",
    val progress: String = "Pending",
    val streakNumber: Int=0,
    val dueDate: Date? = null,
    val dueTime: Time? = null,
    val user: String = ""
)