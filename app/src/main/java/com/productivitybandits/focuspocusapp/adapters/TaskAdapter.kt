package com.productivitybandits.focuspocusapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.productivitybandits.focuspocusapp.R
import com.productivitybandits.focuspocusapp.models.Task


class TaskAdapter(private var tasks: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // 🔁 ViewHolder holds the views for each task card
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleText: TextView = itemView.findViewById(R.id.task_title)
        val timeText: TextView = itemView.findViewById(R.id.task_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task_card, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleText.text = task.title
        holder.timeText.text = task.time
    }

    override fun getItemCount(): Int = tasks.size

    // 🚀 Call this when new data is available. Refreshes the adapter with updated list of tasks
    fun updateData(newData: List<Task>) {
        tasks = newData
        notifyDataSetChanged()
    }
}