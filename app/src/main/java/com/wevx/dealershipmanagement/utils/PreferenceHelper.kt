package com.wevx.dealershipmanagement.utils

import android.annotation.SuppressLint
import android.content.Context

object PreferenceHelper {
    @SuppressLint("UseKtx")
    fun saveSelection(context: Context, key: String, value: Int) {
        val prefs = context.getSharedPreferences("home_selections", Context.MODE_PRIVATE)
        prefs.edit().putInt(key, value).apply()
    }

    fun getAllSelections(context: Context): Map<String, Int> {
        val prefs = context.getSharedPreferences("home_selections", Context.MODE_PRIVATE)
        return mapOf(
            "division_id" to prefs.getInt("division_id", 0),
            "district_id" to prefs.getInt("district_id", 0),
            "subdistrict_id" to prefs.getInt("subdistrict_id", 0),
            "area_id" to prefs.getInt("area_id", 0)
        )
    }
}
