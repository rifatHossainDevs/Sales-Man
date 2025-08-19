package com.wevx.dealershipmanagement.data.dto.authDto.logoutDTO


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class ResponseLogoutDto(
    @SerialName("data")
    val `data`: Data? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("statusCode")
    val statusCode: Int? = null,
    @SerialName("success")
    val success: Boolean? = null
){
    @Keep
    @Serializable
    class Data
}