package com.wevx.dealershipmanagement.data.dto.registrationDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class RequestRegistrationDto(
    @SerialName("email")
    val email: String? = null,
    @SerialName("fullName")
    val fullName: String? = null,
    @SerialName("nidNumber")
    val nidNumber: String? = null,
    @SerialName("password")
    val password: String? = null,
    @SerialName("phone")
    val phone: String? = null,
    @SerialName("userType")
    val userType: String? = null
)