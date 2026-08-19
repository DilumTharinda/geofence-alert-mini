package com.example.geofence_alert_mini

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest

object GeofenceHelper {

    const val GEOFENCE_1_ID = "GEOFENCE_COLOMBO_FORT"
    const val GEOFENCE_1_LAT = 6.9344
    const val GEOFENCE_1_LNG = 79.8451
    const val GEOFENCE_1_RADIUS = 200f

    const val GEOFENCE_2_ID = "GEOFENCE_KELANIYA_UNI"
    const val GEOFENCE_2_LAT = 6.9740
    const val GEOFENCE_2_LNG = 79.9150
    const val GEOFENCE_2_RADIUS = 300f

    fun buildGeofences(): List<Geofence> {
        val g1 = Geofence.Builder()
            .setRequestId(GEOFENCE_1_ID)
            .setCircularRegion(GEOFENCE_1_LAT, GEOFENCE_1_LNG, GEOFENCE_1_RADIUS)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val g2 = Geofence.Builder()
            .setRequestId(GEOFENCE_2_ID)
            .setCircularRegion(GEOFENCE_2_LAT, GEOFENCE_2_LNG, GEOFENCE_2_RADIUS)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        return listOf(g1, g2)
    }

    fun buildGeofencingRequest(geofences: List<Geofence>): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(geofences)
            .build()
    }

    fun getGeofenceDescription(): String {
        return "1. $GEOFENCE_1_ID ($GEOFENCE_1_LAT, $GEOFENCE_1_LNG) r=$GEOFENCE_1_RADIUS\n" +
               "2. $GEOFENCE_2_ID ($GEOFENCE_2_LAT, $GEOFENCE_2_LNG) r=$GEOFENCE_2_RADIUS"
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