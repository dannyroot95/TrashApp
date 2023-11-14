package com.optic.uberclonedriverkotlin.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.Listener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.optic.uberclonedriverkotlin.R
import com.optic.uberclonedriverkotlin.activities.MainActivity
import com.optic.uberclonedriverkotlin.providers.AuthProvider
import com.optic.uberclonedriverkotlin.providers.GeoProvider

class GpsService : Service(),Listener {

    private var isServiceRunning = false
    private var authProvider = AuthProvider()
    private var geoProvider = GeoProvider()
    private var easyWayLocation: EasyWayLocation? = null
    private var myLocationLatLng: LatLng? = null
    private val NOTIFICATION_ID = 99

    private val locationRequest = LocationRequest.create().apply {
        interval = 0
        fastestInterval = 0
        priority = Priority.PRIORITY_HIGH_ACCURACY
        smallestDisplacement = 1f
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (!isServiceRunning) {
            easyWayLocation = EasyWayLocation(this, locationRequest, false, false, this)
            // Marcar el servicio como en ejecución
            easyWayLocation?.endUpdates() // OTROS HILOS DE EJECUCION
            easyWayLocation?.startLocation()
            val notification = createNotification()
            startForeground(NOTIFICATION_ID, notification)
            isServiceRunning = true
        }

        // Indica que el servicio no debe reiniciarse automáticamente
        return START_NOT_STICKY
    }

    override fun locationOn() {
    }

    override fun currentLocation(location: Location) {
        myLocationLatLng = LatLng(location.latitude, location.longitude)
        sendLocationToGeoProvider(authProvider.getId(), myLocationLatLng!!)
    }

    override fun locationCancelled() {
    }


    private fun sendLocationToGeoProvider(driverId: String, location: LatLng) {
        // Lógica para enviar la ubicación a GeoProvider
        geoProvider.saveLocation(driverId, location)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createNotification(): Notification {
        val channelId = "YourChannelId"
        val channelName = "YourChannelName"
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Ruta en ejecución")
            .setContentText("Enviando datos de ubicación")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.icon_pin)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
        easyWayLocation?.endUpdates()
    }

}