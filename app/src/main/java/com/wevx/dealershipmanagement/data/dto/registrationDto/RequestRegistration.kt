package com.wevx.dealershipmanagement.data.dto.registrationDto

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
@Serializable
data class RequestRegistration(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("fullName")
    val fullName: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("userType")
    val userType: String? = null
)