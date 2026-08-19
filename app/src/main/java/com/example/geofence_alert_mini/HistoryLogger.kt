package com.example.geofence_alert_mini

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class HistoryEvent(val type: String, val timestamp: String)

object HistoryLogger {

    private val events = mutableListOf<HistoryEvent>()
    private val listeners = mutableListOf<() -> Unit>()

    fun addEvent(type: String) {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val timestamp = formatter.format(Date())
        events.add(0, HistoryEvent(type, timestamp))
        listeners.forEach { it.invoke() }
    }

    fun getEvents(): List<HistoryEvent> = events

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }
}