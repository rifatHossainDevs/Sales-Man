package com.wevx.dealershipmanagement.data.createStoreDTO


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.wevx.dealershipmanagement.domain.models.CreateStoreModel

@Keep
@Serializable
data class ResponseCreateStoreDTO(
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
        @SerializedName("address")
        val address: String? = null,
        @SerializedName("areaNo")
        val areaNo: Int? = null,
        @SerializedName("avatar")
        val avatar: String? = null,
        @SerializedName("coordinates")
        val coordinates: List<String?>? = null,
        @SerializedName("createdAt")
        val createdAt: String? = null,
        @SerializedName("_id")
        val id: String? = null,
        @SerializedName("phone")
        val phone: String? = null,
        @SerializedName("storeName")
        val storeName: String? = null,
        @SerializedName("storeOwnerName")
        val storeOwnerName: String? = null,
        @SerializedName("storePictures")
        val storePictures: String? = null,
        @SerializedName("subDisNo")
        val subDisNo: Int? = null,
        @SerializedName("underCompanyId")
        val underCompanyId: String? = null,
        @SerializedName("updatedAt")
        val updatedAt: String? = null,
        @SerializedName("userId")
        val userId: String? = null,
        @SerializedName("__v")
        val v: Int? = null
    )
}

fun ResponseCreateStoreDTO.toCreateStoreModel(): CreateStoreModel {
    return CreateStoreModel(
        message = message ?: "",
        status = success ?: false
    )
}

