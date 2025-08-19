package com.wevx.dealershipmanagement.data.dto.authDto.updateProfileDto

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
@Serializable
data class RequestUpdateProfile(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("fullName")
    val fullName: String? = null,
    @SerializedName("nidNumber")
    val nidNumber: String? = null,
    @SerializedName("phone")
    val phone: String? = null
)