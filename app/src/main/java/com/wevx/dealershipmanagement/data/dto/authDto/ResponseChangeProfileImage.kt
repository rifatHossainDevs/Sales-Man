package com.wevx.dealershipmanagement.data.dto.authDto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ResponseChangeProfileImage(
    @SerialName("data")
    val `data`: Data?,
    @SerialName("message")
    val message: String?,
    @SerialName("statusCode")
    val statusCode: Int?,
    @SerialName("success")
    val success: Boolean?
) {
    @Keep
    @Serializable
    data class Data(
        @SerialName("active")
        val active: Boolean?,
        @SerialName("avatar")
        val avatar: String?,
        @SerialName("companyId")
        val companyId: String?,
        @SerialName("createdAt")
        val createdAt: String?,
        @SerialName("email")
        val email: String?,
        @SerialName("fullName")
        val fullName: String?,
        @SerialName("_id")
        val id: String?,
        @SerialName("nidNumber")
        val nidNumber: String?,
        @SerialName("phone")
        val phone: String?,
        @SerialName("updatedAt")
        val updatedAt: String?,
        @SerialName("userType")
        val userType: String?,
        @SerialName("__v")
        val v: Int?
    )
}