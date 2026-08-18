package com.example.geofence_alert_mini

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private lateinit var geofencingClient: GeofencingClient

    private fun registerGeofence() {
        geofencingClient = LocationServices.getGeofencingClient(this)
        val geofence = GeofenceHelper.buildGeofence()
        val request = GeofenceHelper.buildGeofencingRequest(geofence)
        val pendingIntent = GeofenceHelper.buildGeofencePendingIntent(this)

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        geofencingClient.addGeofences(request, pendingIntent)
            .addOnSuccessListener {
                // Geofence registered successfully
            }
            .addOnFailureListener { e ->
                // Registration failed, e.message has the reason
            }
    }
}