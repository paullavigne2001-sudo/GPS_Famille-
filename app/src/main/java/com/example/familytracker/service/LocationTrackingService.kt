package com.example.familytracker.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.familytracker.data.model.LocationPoint
import com.example.familytracker.data.repository.AuthRepository
import com.example.familytracker.data.repository.LocationRepository
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Foreground Service obligatoire (Android 8+) pour un suivi GPS fiable en arrière-plan.
 * Notification persistante = transparence : l'enfant voit toujours que le suivi est actif.
 */
class LocationTrackingService : Service() {

    private lateinit var fusedClient: FusedLocationProviderClient
    private val scope = CoroutineScope(Dispatchers.IO)
    private val authRepo = AuthRepository()
    private val locationRepo = LocationRepository()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val location = result.lastLocation ?: return
            val uid = authRepo.currentUid() ?: return
            scope.launch {
                val profile = authRepo.fetchProfile(uid)
                locationRepo.pushLocation(
                    familyId = profile.familyId,
                    point = LocationPoint(
                        userId = uid,
                        lat = location.latitude,
                        lng = location.longitude,
                        timestamp = System.currentTimeMillis(),
                        batteryLevel = -1 // TODO: lire via BatteryManager
                    )
                )
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        fusedClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, buildNotification())
        startLocationUpdates()
        return START_STICKY
    }

    private fun startLocationUpdates() {
        val request = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            UPDATE_INTERVAL_MS
        ).setMinUpdateIntervalMillis(MIN_UPDATE_INTERVAL_MS).build()

        // Nécessite ACCESS_FINE_LOCATION + ACCESS_BACKGROUND_LOCATION déjà accordées.
        fusedClient.requestLocationUpdates(request, locationCallback, mainLooper)
    }

    private fun buildNotification(): Notification {
        val channelId = "location_tracking"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Suivi de localisation",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Suivi de localisation actif")
            .setContentText("Ta position est partagée avec ta famille.")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        fusedClient.removeLocationUpdates(locationCallback)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val UPDATE_INTERVAL_MS = 30_000L
        private const val MIN_UPDATE_INTERVAL_MS = 15_000L

        fun start(context: Context) {
            context.startForegroundService(Intent(context, LocationTrackingService::class.java))
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, LocationTrackingService::class.java))
        }
    }
}
