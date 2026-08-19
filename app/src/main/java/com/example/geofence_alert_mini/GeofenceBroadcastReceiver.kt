package com.example.geofence_alert_mini

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

/**
 * Receiver that handles Geofencing events triggered by the system.
 */
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e(TAG, "Geofencing Error: $errorMessage")
            return
        }

        val transitionName = GeofenceHelper.getTransitionName(geofencingEvent.geofenceTransition)
        val geofenceIds = geofencingEvent.triggeringGeofences?.joinToString { it.requestId } ?: "Unknown"
        val message = "$transitionName: $geofenceIds"
        
        Log.d(TAG, "Geofence Event: $message")
        
        // Notify the user
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        NotificationHelper.showGeofenceNotification(context, message)

        // Update history
        HistoryLogger.addEvent(message)
    }

    companion object {
        private const val TAG = "GeofenceReceiver"
    }
}
