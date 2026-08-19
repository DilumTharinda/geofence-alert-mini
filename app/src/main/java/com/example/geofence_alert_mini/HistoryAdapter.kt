package com.example.geofence_alert_mini

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private val items: List<HistoryEvent>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val type: TextView = view.findViewById(R.id.tvEventType)
        val time: TextView = view.findViewById(R.id.tvEventTime)
        val icon: ImageView = view.findViewById(R.id.ivEventIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.type.text = item.type
        holder.time.text = item.timestamp

        val iconRes = when {
            item.type.contains("Entered", true) -> android.R.drawable.ic_menu_myplaces
            item.type.contains("Exited", true) -> android.R.drawable.ic_menu_directions
            else -> android.R.drawable.ic_dialog_map
        }
        holder.icon.setImageResource(iconRes)
    }

    override fun getItemCount(): Int = items.size
}