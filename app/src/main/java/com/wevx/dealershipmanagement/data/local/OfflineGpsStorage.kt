package com.wevx.dealershipmanagement.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wevx.dealershipmanagement.data.remote.gps.OfflineRoutePointDto
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class OfflineGpsStorage(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("offline_gps_queue_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val keyQueue = "offline_gps_points_key"
    private val keyLastOfflineSaveMs = "last_offline_save_ms_key"
    private val keyLastKnownLat = "last_known_lat_key"
    private val keyLastKnownLng = "last_known_lng_key"
    private val keyLastKnownPlace = "last_known_place_key"

    companion object {
        // Minimum 5-minute spacing for offline route points during working hours (9 AM - 6 PM BD time)
        private const val OFFLINE_ROUTE_INTERVAL_MS = 5 * 60 * 1000L

        fun isBangladeshWorkingHours(date: Date = Date()): Boolean {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Dhaka"))
            calendar.time = date
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            // Working hours: 9:00 AM (09:00) to 6:00 PM (17:59:59)
            return hour in 9..17
        }
    }

    @Synchronized
    fun saveLastKnownLocation(lat: Double, lng: Double, placeName: String) {
        prefs.edit()
            .putString(keyLastKnownLat, lat.toString())
            .putString(keyLastKnownLng, lng.toString())
            .putString(keyLastKnownPlace, placeName)
            .apply()
    }

    @Synchronized
    fun getLastKnownLocation(): Triple<Double, Double, String>? {
        val latStr = prefs.getString(keyLastKnownLat, null) ?: return null
        val lngStr = prefs.getString(keyLastKnownLng, null) ?: return null
        val place = prefs.getString(keyLastKnownPlace, "In Market") ?: "In Market"
        val lat = latStr.toDoubleOrNull() ?: return null
        val lng = lngStr.toDoubleOrNull() ?: return null
        return Triple(lat, lng, place)
    }

    /**
     * Saves an offline route point ONLY if it falls within working hours (9 AM - 6 PM BD time)
     * and at least 5 minutes have elapsed since the previous saved offline route point.
     */
    @Synchronized
    fun savePointIfEligible(point: OfflineRoutePointDto, nowMs: Long = System.currentTimeMillis()): Boolean {
        saveLastKnownLocation(point.latitude, point.longitude, point.placeName ?: "In Market")

        if (!isBangladeshWorkingHours(Date(nowMs))) {
            return false
        }

        val lastSavedMs = prefs.getLong(keyLastOfflineSaveMs, 0L)
        if (lastSavedMs > 0L && (nowMs - lastSavedMs) < OFFLINE_ROUTE_INTERVAL_MS) {
            return false
        }

        val points = getSavedPoints().toMutableList()
        points.add(point)
        val json = gson.toJson(points)
        prefs.edit()
            .putString(keyQueue, json)
            .putLong(keyLastOfflineSaveMs, nowMs)
            .apply()
        return true
    }

    @Synchronized
    fun savePoint(point: OfflineRoutePointDto) {
        savePointIfEligible(point, System.currentTimeMillis())
    }

    @Synchronized
    fun getSavedPoints(): List<OfflineRoutePointDto> {
        val json = prefs.getString(keyQueue, null) ?: return emptyList()
        val type = object : TypeToken<List<OfflineRoutePointDto>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    @Synchronized
    fun clearQueue() {
        prefs.edit().remove(keyQueue).apply()
    }
}
