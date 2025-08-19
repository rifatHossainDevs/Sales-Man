package com.wevx.dealershipmanagement.data.dto.authDto.registrationDto

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
@Serializable
data class RequestRegistrationDto(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("fullName")
    val fullName: String? = null,
    @SerializedName("nidNumber")
    val nidNumber: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("userType")
    val userType: String? = null,
    @SerializedName("companyId")
    val companyId: String? = null,
)