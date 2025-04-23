package com.productivitybandits.focuspocusapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.productivitybandits.focuspocusapp.R
import com.productivitybandits.focuspocusapp.calendar.CalendarEvent

class CalendarEventAdapter(
    private val events: List<CalendarEvent>,
    private val onConfirm: (CalendarEvent) -> Unit,
    private val onDismiss: (CalendarEvent) -> Unit
) : RecyclerView.Adapter<CalendarEventAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val confirmButton: Button = view.findViewById(R.id.btn_confirm)
        val dismissButton: Button = view.findViewById(R.id.btn_dismiss)
        val eventTitle: TextView = view.findViewById(R.id.task_title)

        fun bind(event: CalendarEvent) {
            eventTitle.text = event.title  // Bind event title

            confirmButton.setOnClickListener {
                onConfirm(event)  // Confirm button action
            }

            dismissButton.setOnClickListener {
                onDismiss(event)  // Dismiss button action
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    override fun getItemCount() = events.size
}

