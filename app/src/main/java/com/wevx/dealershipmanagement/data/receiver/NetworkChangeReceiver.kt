package com.wevx.dealershipmanagement.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.wevx.dealershipmanagement.data.local.OfflineGpsStorage
import com.wevx.dealershipmanagement.data.remote.gps.GpsApiService
import com.wevx.dealershipmanagement.data.remote.gps.LiveGpsRequest
import com.wevx.dealershipmanagement.data.remote.gps.SyncOfflineRoutesRequest
import com.wevx.dealershipmanagement.data.service.LocationForegroundService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NetworkChangeReceiver : BroadcastReceiver() {

    @Inject
    lateinit var gpsApiService: GpsApiService

    override fun onReceive(context: Context, intent: Intent?) {
        // If phone just booted up or app was updated while salesman is logged in, restart tracking service
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED ||
            intent?.action == Intent.ACTION_MY_PACKAGE_REPLACED
        ) {
            LocationForegroundService.startService(context)
        }

        if (isNetworkConnected(context)) {
            Log.d("NetworkReceiver", "Device came online! Checking for offline GPS queue...")
            val storage = OfflineGpsStorage(context.applicationContext)
            val queuedPoints = storage.getSavedPoints()
            val lastKnown = storage.getLastKnownLocation()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    if (queuedPoints.isNotEmpty()) {
                        val response = gpsApiService.syncOfflineRoutes(SyncOfflineRoutesRequest(queuedPoints))
                        if (response.isSuccessful) {
                            storage.clearQueue()
                            Log.d("NetworkReceiver", "Successfully uploaded ${queuedPoints.size} offline GPS points.")
                        }
                    }
                    if (lastKnown != null) {
                        gpsApiService.updateLiveLocation(
                            LiveGpsRequest(lastKnown.first, lastKnown.second, lastKnown.third)
                        )
                    }
                } catch (e: Exception) {
                    Log.e("NetworkReceiver", "Failed to sync offline GPS points on reconnection", e)
                }
            }
        }
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(activeNetwork) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
