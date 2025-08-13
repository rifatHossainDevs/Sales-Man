package com.wevx.dealershipmanagement.data.dto.profileDTO

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.wevx.dealershipmanagement.domain.models.ProfileModel

@Keep
@Serializable
data class ResponseProfileDto(
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


fun ResponseProfileDto.Data.toProfileModel(): ProfileModel {
    return ProfileModel(
        id = this.id ?: "",
        name = this.fullName ?: "",
        email = this.email ?: "",
        phone = this.phone ?: "",
        nid = this.nidNumber ?: "",
        avatar = this.avatar ?: "",
        userType = this.userType ?: "",
        companyId = this.companyId ?: ""
    )
}