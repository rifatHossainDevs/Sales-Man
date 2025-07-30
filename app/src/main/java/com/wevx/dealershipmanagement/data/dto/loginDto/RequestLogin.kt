package com.wevx.dealershipmanagement.data.dto.loginDto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class RequestLogin(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("password")
    val password: String? = null
)