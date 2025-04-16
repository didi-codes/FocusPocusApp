package com.productivitybandits.taskmanager

import java.time.LocalDate
import java.time.LocalTime

data class Task(
    val title: String = "",
    val description: String = "",
    val progress: String = "Pending",
    val streakNumber: Int = 0,
    val dueDate: LocalDate? = null,
    val dueTime: LocalTime? = null,
    val user: String = ""
)