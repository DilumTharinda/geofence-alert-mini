package com.example.geofence_alert_mini

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent == null) {
            Log.e("GeofenceReceiver", "GeofencingEvent is null")
            return
        }

        if (geofencingEvent.hasError()) {
            Log.e("GeofenceReceiver", "GeofencingEvent error: ${geofencingEvent.errorCode}")
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT
        ) {
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            Log.i("GeofenceReceiver", "Geofence transition: $geofenceTransition")
        } else {
            Log.e("GeofenceReceiver", "Invalid transition type: $geofenceTransition")
        }
    }
}
