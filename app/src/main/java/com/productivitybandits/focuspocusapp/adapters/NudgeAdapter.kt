package com.productivitybandits.focuspocusapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.productivitybandits.focuspocusapp.R
import com.productivitybandits.focuspocusapp.models.Nudge


class NudgeAdapter(private var nudges: List<Nudge>) :
    RecyclerView.Adapter<NudgeAdapter.NudgeViewHolder>() {

    class NudgeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.nudge_title)
        val descText: TextView = itemView.findViewById(R.id.nudge_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NudgeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nudge_card, parent, false)
        return NudgeViewHolder(view)
    }

    override fun onBindViewHolder(holder: NudgeViewHolder, position: Int) {
        val nudge = nudges[position]
        holder.titleText.text = nudge.title
        holder.descText.text = nudge.description
    }

    override fun getItemCount() = nudges.size

    fun updateData(newData: List<Nudge>) {
        nudges = newData
        notifyDataSetChanged()
    }
}