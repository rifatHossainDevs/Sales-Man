package com.wevx.dealershipmanagement.data.service

import android.Manifest
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.os.PowerManager
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.wevx.dealershipmanagement.data.local.OfflineGpsStorage
import com.wevx.dealershipmanagement.data.remote.gps.GpsApiService
import com.wevx.dealershipmanagement.data.remote.gps.LiveGpsRequest
import com.wevx.dealershipmanagement.data.remote.gps.OfflineRoutePointDto
import com.wevx.dealershipmanagement.data.remote.gps.SyncOfflineRoutesRequest
import com.wevx.dealershipmanagement.presentation.MainActivity
import com.wevx.dealershipmanagement.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@AndroidEntryPoint
class LocationForegroundService : Service() {

    @Inject
    lateinit var gpsApiService: GpsApiService

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var offlineStorage: OfflineGpsStorage
    private lateinit var connectivityManager: ConnectivityManager
    private var wakeLock: PowerManager.WakeLock? = null

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val CHANNEL_ID = "SalesmanLocationChannel"
    private val NOTIFICATION_ID = 1001

    // Send live GPS updates every 30 seconds (24/7 while logged in, even when screen is locked or app is closed).
    // Backend automatically records RoutePoint breadcrumbs every 5-10 minutes during working hours (9 AM - 6 PM BD time).
    private val LIVE_UPDATE_INTERVAL_MS = 30 * 1000L
    private val FASTEST_INTERVAL_MS = 15 * 1000L

    companion object {
        const val ACTION_START_TRACKING = "com.wevx.dealershipmanagement.action.START_TRACKING"
        const val ACTION_STOP_TRACKING = "com.wevx.dealershipmanagement.action.STOP_TRACKING"

        fun startService(context: Context) {
            val token = TokenManager(context.applicationContext).getAccessToken()
            if (token.isNullOrBlank()) return

            val hasFine = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            val hasCoarse = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasFine && !hasCoarse) {
                Log.w("LocationService", "Cannot start LocationForegroundService: location permission not granted yet")
                return
            }

            try {
                val intent = Intent(context.applicationContext, LocationForegroundService::class.java).apply {
                    action = ACTION_START_TRACKING
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.applicationContext.startForegroundService(intent)
                } else {
                    context.applicationContext.startService(intent)
                }
            } catch (e: Exception) {
                Log.e("LocationService", "Failed to start LocationForegroundService", e)
            }
        }

        fun stopService(context: Context) {
            try {
                val intent = Intent(context.applicationContext, LocationForegroundService::class.java).apply {
                    action = ACTION_STOP_TRACKING
                }
                context.applicationContext.stopService(intent)
            } catch (e: Exception) {
                Log.e("LocationService", "Failed to stop LocationForegroundService", e)
            }
        }
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d("LocationService", "Network became available! Flushing offline queue & updating live status...")
            serviceScope.launch {
                flushOfflineQueueAndSendLatest()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        offlineStorage = OfflineGpsStorage(applicationContext)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        acquireWakeLock()
        createNotificationChannel()
        val notification = buildNotification()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
                )
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        } catch (e: Exception) {
            Log.e("LocationService", "Error calling startForeground", e)
        }

        registerNetworkCallback()
        setupLocationUpdates()
    }

    private fun acquireWakeLock() {
        try {
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "DealershipManagement:LocationWakeLock"
            ).apply {
                setReferenceCounted(false)
                acquire()
            }
        } catch (e: Exception) {
            Log.e("LocationService", "Failed to acquire WakeLock", e)
        }
    }

    private fun registerNetworkCallback() {
        try {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager.registerNetworkCallback(request, networkCallback)
        } catch (e: Exception) {
            Log.e("LocationService", "Failed to register NetworkCallback", e)
        }
    }

    private fun setupLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LIVE_UPDATE_INTERVAL_MS)
            .setMinUpdateIntervalMillis(FASTEST_INTERVAL_MS)
            .setWaitForAccurateLocation(false)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation ?: return
                val lat = location.latitude
                val lng = location.longitude

                Log.d("LocationService", "Captured GPS Location: lat=$lat, lng=$lng")
                handleNewLocation(lat, lng)
            }
        }

        try {
            // 1. Immediately push last known location so Admin Panel sees salesman online right away
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    handleNewLocation(loc.latitude, loc.longitude)
                }
            }

            // 2. Request continuous background/screen-locked updates
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            Log.e("LocationService", "Missing location permissions", e)
        }
    }

    private fun resolvePlaceName(lat: Double, lng: Double): String {
        return try {
            val geocoder = Geocoder(applicationContext, Locale.ENGLISH)
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                val subLocality = addr.subLocality ?: addr.thoroughfare
                val locality = addr.locality ?: addr.subAdminArea ?: "Dhaka"
                if (!subLocality.isNullOrBlank() && subLocality != locality) {
                    "$subLocality, $locality"
                } else {
                    locality
                }
            } else {
                "In Market"
            }
        } catch (_: Exception) {
            "In Market"
        }
    }

    private fun handleNewLocation(lat: Double, lng: Double) {
        val nowMs = System.currentTimeMillis()
        val timestampIso = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date(nowMs))

        serviceScope.launch {
            val placeName = resolvePlaceName(lat, lng)
            offlineStorage.saveLastKnownLocation(lat, lng, placeName)

            if (isNetworkConnected()) {
                try {
                    // 1. Send Live Location Update to Backend
                    // (Backend updates liveLat/liveLng/isOnline 24/7, and saves RoutePoint every 5-10 min during 9AM-6PM)
                    val liveResponse = gpsApiService.updateLiveLocation(
                        LiveGpsRequest(lat, lng, placeName)
                    )

                    if (!liveResponse.isSuccessful) {
                        Log.w("LocationService", "Live location HTTP ${liveResponse.code()}, queueing locally if in working hours")
                        offlineStorage.savePointIfEligible(
                            OfflineRoutePointDto(lat, lng, placeName, timestampIso),
                            nowMs
                        )
                    } else {
                        // 2. Also flush any pending offline queued points
                        flushOfflineQueueOnly()
                    }
                } catch (e: Exception) {
                    Log.e("LocationService", "Error posting live location, queueing locally", e)
                    offlineStorage.savePointIfEligible(
                        OfflineRoutePointDto(lat, lng, placeName, timestampIso),
                        nowMs
                    )
                }
            } else {
                // Device is offline -> Save locally if within working hours (9 AM - 6 PM) & >= 5 min interval
                val saved = offlineStorage.savePointIfEligible(
                    OfflineRoutePointDto(lat, lng, placeName, timestampIso),
                    nowMs
                )
                Log.d("LocationService", "Device offline. Queued working-hour point=$saved (total queued=${offlineStorage.getSavedPoints().size})")
            }
        }
    }

    private suspend fun flushOfflineQueueOnly() {
        val queuedPoints = offlineStorage.getSavedPoints()
        if (queuedPoints.isNotEmpty()) {
            try {
                val response = gpsApiService.syncOfflineRoutes(SyncOfflineRoutesRequest(queuedPoints))
                if (response.isSuccessful) {
                    offlineStorage.clearQueue()
                    Log.d("LocationService", "Successfully synced ${queuedPoints.size} offline points")
                }
            } catch (e: Exception) {
                Log.e("LocationService", "Failed to flush offline queue", e)
            }
        }
    }

    private suspend fun flushOfflineQueueAndSendLatest() {
        flushOfflineQueueOnly()
        val lastKnown = offlineStorage.getLastKnownLocation()
        if (lastKnown != null) {
            try {
                gpsApiService.updateLiveLocation(
                    LiveGpsRequest(lastKnown.first, lastKnown.second, lastKnown.third)
                )
            } catch (e: Exception) {
                Log.e("LocationService", "Failed to send latest live location on reconnect", e)
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val caps = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Salesman Location Tracking",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Background GPS location tracking for field sales operations"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Sales-Man Live Tracking Active")
            .setContentText("Sharing live location & 9AM–6PM route history with Dealership")
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP_TRACKING) {
            stopSelf()
            return START_NOT_STICKY
        }
        return START_STICKY
    }

    /**
     * Ensures that if the user swipes the app away from Recents while still logged in,
     * the foreground location service automatically restarts within 3 seconds.
     */
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        val token = TokenManager(applicationContext).getAccessToken()
        if (!token.isNullOrBlank()) {
            try {
                val restartIntent = Intent(applicationContext, LocationForegroundService::class.java).apply {
                    setPackage(packageName)
                    action = ACTION_START_TRACKING
                }
                val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    PendingIntent.getForegroundService(
                        applicationContext,
                        1002,
                        restartIntent,
                        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                    )
                } else {
                    PendingIntent.getService(
                        applicationContext,
                        1002,
                        restartIntent,
                        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                    )
                }
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.set(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 3000L,
                    pendingIntent
                )
            } catch (e: Exception) {
                Log.e("LocationService", "Failed to schedule restart in onTaskRemoved", e)
            }
        }
    }

    override fun onDestroy() {
        try {
            if (::locationCallback.isInitialized) {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        } catch (_: Exception) {}

        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } catch (_: Exception) {}

        try {
            if (wakeLock?.isHeld == true) {
                wakeLock?.release()
            }
        } catch (_: Exception) {}

        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
