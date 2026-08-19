package com.example.geofence_alert_mini

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

/**
 * Receiver that handles Geofencing events triggered by the system.
 */
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

        if (geofencingEvent.hasError()) {
            handleError(geofencingEvent.errorCode)
            return
        }

        val message = getTransitionMessage(geofencingEvent)
        
        // Log the event
        Log.d(TAG, "Geofence Event: $message")
        
        // Notify the user via UI
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        NotificationHelper.showGeofenceNotification(context, message)

        // Persistence/State update
        HistoryLogger.addEvent(message)
    }

    private fun handleError(errorCode: Int) {
        val errorMessage = GeofenceStatusCodes.getStatusCodeString(errorCode)
        Log.e(TAG, "Geofencing Error: $errorMessage")
    }

    private fun getTransitionMessage(geofencingEvent: GeofencingEvent): String {
        val transitionType = geofencingEvent.geofenceTransition
        val triggeringGeofences = geofencingEvent.triggeringGeofences
        val geofenceIds = triggeringGeofences?.joinToString { it.requestId } ?: "Unknown"

        val transitionName = when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> "Entered"
            Geofence.GEOFENCE_TRANSITION_EXIT -> "Exited"
            Geofence.GEOFENCE_TRANSITION_DWELL -> "Dwelling in"
            else -> "Unknown transition for"
        }

        return "$transitionName: $geofenceIds"
    }

    companion object {
        private const val TAG = "GeofenceReceiver"
    }
}
