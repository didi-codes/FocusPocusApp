package com.productivitybandits.focuspocusapp.calendar

data class CalendarEvent(
    val id: Long,
    val title: String,
    val startTimeMillis: Long,
    val endTimeMillis: Long
)