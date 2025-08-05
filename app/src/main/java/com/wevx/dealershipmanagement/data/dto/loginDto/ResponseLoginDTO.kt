package com.wevx.dealershipmanagement.data.dto.loginDto

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.wevx.dealershipmanagement.domain.models.LoginModel
import kotlinx.serialization.Serializable

@Keep
data class ResponseLoginDTO(
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
        @SerializedName("accessToken")
        val accessToken: String? = null,
        @SerializedName("refreshToken")
        val refreshToken: String? = null,
        @SerializedName("user")
        val user: User? = null
    ) {
        @Keep
        @Serializable
        data class User(
            @SerializedName("active")
            val active: Boolean? = null,
            @SerializedName("avatar")
            val avatar: String? = null,
            @SerializedName("companyId")
            val companyId: String? = null,
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
            @SerializedName("__v")
            val v: Int? = null
        )
    }
}


fun ResponseLoginDTO.Data.toUserModel(): LoginModel {
    return LoginModel(
        accessToken = this.accessToken ?: "",
        refreshToken = this.refreshToken ?: "",
        userProfileImageUrl = this.user?.avatar ?: "",
        userEmail = this.user?.email ?: "",
        userName = this.user?.fullName ?: "",
        userId = this.user?.id ?: "",
        nidNumber = this.user?.nidNumber ?: "",
        userPhoneNumber = this.user?.phone ?: "",
        userType = this.user?.userType ?: "",
        companyId = this.user?.companyId ?: "",
        isActive = this.user?.active ?: false
    )
}