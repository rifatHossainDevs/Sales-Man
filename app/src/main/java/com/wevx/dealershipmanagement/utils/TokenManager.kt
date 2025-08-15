package com.wevx.dealershipmanagement.utils

import android.content.Context
import androidx.core.content.edit
import com.wevx.dealershipmanagement.utils.Constants.ACCESS_TOKEN
import com.wevx.dealershipmanagement.utils.Constants.REFRESH_TOKEN

class TokenManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("saveNote", Context.MODE_PRIVATE)

    fun saveToken(accessToken: String, refreshToken: String) {
        sharedPreferences.edit(commit = true) {
            putString(ACCESS_TOKEN, accessToken)
            putString(REFRESH_TOKEN, refreshToken)
        }
    }

    fun getAccessToken(): String? = sharedPreferences.getString(ACCESS_TOKEN, null)

    fun getRefreshToken(): String? = sharedPreferences.getString(REFRESH_TOKEN, null)

    fun clearTokens() {
        sharedPreferences.edit { clear() }
    }

    fun hasValidTokens(): Boolean {
        val access = getAccessToken()
        val refresh = getRefreshToken()
        return !access.isNullOrBlank() && !refresh.isNullOrBlank()
    }
}
