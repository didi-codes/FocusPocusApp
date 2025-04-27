package com.productivitybandits.focuspocusapp.calendar

import android.content.Context
import android.provider.CalendarContract
import java.util.Calendar

object CalendarHelper {
    fun fetchCalendarEvents(context: Context): List<CalendarEvent> {
        val events = mutableListOf<CalendarEvent>()

        val projection = arrayOf(
            CalendarContract.Events._ID,  // Include the event ID
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
        )

        val calendarUri = CalendarContract.Events.CONTENT_URI
        val selection = "${CalendarContract.Events.DTSTART} IS NOT NULL"

        val cursor = context.contentResolver.query(
            calendarUri,
            projection,
            selection,
            null,
            CalendarContract.Events.DTSTART + " ASC"
        )

        cursor?.use {
            val idIdx = it.getColumnIndex(CalendarContract.Events._ID)
            val titleIdx = it.getColumnIndex(CalendarContract.Events.TITLE)
            val startIdx = it.getColumnIndex(CalendarContract.Events.DTSTART)
            val endIdx = it.getColumnIndex(CalendarContract.Events.DTEND)

            while (it.moveToNext()) {
                val id = it.getLong(idIdx)
                val title = it.getString(titleIdx) ?: "No Title"
                val start = it.getLong(startIdx)
                val end = it.getLong(endIdx)

                events.add(CalendarEvent(id, title, start, end))
            }
        }

        // Separate today's events from older ones
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        val startOfToday = today.timeInMillis

        val endOfToday = startOfToday + 24 * 60 * 60 * 1000 // Add 1 day

        val todayEvents = events.filter {
            it.startTimeMillis in startOfToday until endOfToday
        }

        val olderEvents = events.filter {
            it.startTimeMillis < startOfToday
        }

        return todayEvents + olderEvents // today first, then older
    }
}
