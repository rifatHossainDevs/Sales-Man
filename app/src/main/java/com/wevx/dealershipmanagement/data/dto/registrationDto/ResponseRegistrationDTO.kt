package com.wevx.dealershipmanagement.data.dto.registrationDto

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.wevx.dealershipmanagement.domain.models.RegistrationModel

@Keep
@Serializable
data class ResponseRegistrationDTO(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("statusCode")
    val statusCode: Int? = null,
    @SerializedName("success")
    val success: Boolean? = null
) {
    @Keep
    @Serializable
    data class Data(
        @SerializedName("avatar")
        val avatar: String? = null,
        @SerializedName("createdAt")
        val createdAt: String? = null,
        @SerializedName("email")
        val email: String? = null,
        @SerializedName("fullName")
        val fullName: String? = null,
        @SerializedName("_id")
        val id: String? = null,
        @SerializedName("nidNumber")
        val nidNumber: String? = null,
        @SerializedName("phone")
        val phone: String? = null,
        @SerializedName("updatedAt")
        val updatedAt: String? = null,
        @SerializedName("userType")
        val userType: String? = null,
        @SerializedName("companyId")
        val companyId: String? = null,
        @SerializedName("active")
        val active: Boolean? = null,
        @SerializedName("__v")
        val v: Int? = null
    )
}

fun ResponseRegistrationDTO.Data.toRegistrationModel(): RegistrationModel {
    return RegistrationModel(
        userId = this.id ?: "",
        userEmail = this.email ?: "",
        userName = this.fullName ?: "",
        userPhoneNumber = this.phone ?: "",
        userProfileImageUrl = this.avatar ?: "",
        userRole = this.userType ?: "",
        nid = this.nidNumber ?: "",
        companyId = this.companyId ?: "",
        active = this.active ?: false
    )
}