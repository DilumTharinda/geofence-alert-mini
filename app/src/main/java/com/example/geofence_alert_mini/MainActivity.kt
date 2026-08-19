package com.example.geofence_alert_mini

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private val locationPermissionRequestCode = 1001
    private val backgroundPermissionRequestCode = 1002

    private lateinit var tvActiveGeofence: TextView
    private lateinit var rvHistory: RecyclerView
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvActiveGeofence = findViewById(R.id.tvActiveGeofence)
        rvHistory = findViewById(R.id.rvHistory)

        setupHistoryList()
        checkAndRequestPermissions()
    }

    private fun setupHistoryList() {
        adapter = HistoryAdapter(HistoryLogger.getEvents())
        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = adapter

        HistoryLogger.addListener {
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionRequestCode
            )
        } else {
            checkBackgroundPermission()
        }
    }

    private fun checkBackgroundPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    backgroundPermissionRequestCode
                )
            } else {
                registerGeofence()
            }
        } else {
            registerGeofence()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            locationPermissionRequestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkBackgroundPermission()
                }
            }
            backgroundPermissionRequestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    registerGeofence()
                }
            }
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
                tvActiveGeofence.text = "Active Geofence: ${GeofenceHelper.GEOFENCE_ID}"
            }
            .addOnFailureListener { e ->
                tvActiveGeofence.text = "Active Geofence: Failed to register (${e.message})"
            }
    }
}
