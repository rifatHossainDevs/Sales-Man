package com.wevx.dealershipmanagement.data.dto.loginDto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class RequestLogin(
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("password")
    val password: String? = null
)