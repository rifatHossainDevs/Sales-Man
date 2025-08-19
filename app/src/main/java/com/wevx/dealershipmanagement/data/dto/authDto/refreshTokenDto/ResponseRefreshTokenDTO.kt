package com.wevx.dealershipmanagement.data.dto.authDto.refreshTokenDto

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
@Serializable
data class ResponseRefreshTokenDTO(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("statusCode")
    val statusCode: Int? = null,
    @SerializedName("success")
    val success: Boolean? = null
){
    @Keep
    @Serializable
    data class Data(
        @SerializedName("accessToken")
        val accessToken: String? = null,
        @SerializedName("refreshToken")
        val refreshToken: String? = null
    )
}