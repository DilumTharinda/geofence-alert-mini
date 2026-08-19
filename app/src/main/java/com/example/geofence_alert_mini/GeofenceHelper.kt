package com.example.geofence_alert_mini

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest

object GeofenceHelper {

    const val GEOFENCE_ID = "MINI_GEOFENCE_1"
    const val GEOFENCE_LAT = 6.9271
    const val GEOFENCE_LNG = 79.8612
    const val GEOFENCE_RADIUS_METERS = 200f

    fun buildGeofence(): Geofence {
        return Geofence.Builder()
            .setRequestId(GEOFENCE_ID)
            .setCircularRegion(GEOFENCE_LAT, GEOFENCE_LNG, GEOFENCE_RADIUS_METERS)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()
    }

    fun buildGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()
    }

    fun buildGeofencePendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }
}