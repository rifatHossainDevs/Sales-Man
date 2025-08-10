package com.wevx.dealershipmanagement.data.dto.getStoreOwnerByAreaDTO

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.wevx.dealershipmanagement.domain.models.Coordinates
import com.wevx.dealershipmanagement.domain.models.StoreOwnerModel

@Keep
@Serializable
data class ResponseStoreOwnerDto(
    @SerializedName("data")
    val `data`: List<Data?>? = null,
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
        @SerializedName("updatedAt")
        val updatedAt: String? = null,
        @SerializedName("userId")
        val userId: String? = null,
        @SerializedName("__v")
        val v: Int? = null
    )
}

fun ResponseStoreOwnerDto.Data.toStoreOwnerModel():  StoreOwnerModel{
    return StoreOwnerModel(
        id = this.id ?: "",
        userId = this.userId ?: "",
        storeOwnerName = this.storeOwnerName ?: "",
        phone = this.phone ?: "",
        storeOwnerAvatar = this.avatar ?: "",
        storeName = this.storeName ?: "",
        storeImg = this.storePictures ?: "",
        coordinates = Coordinates(
            this.coordinates?.getOrNull(0)?.toDoubleOrNull() ?: 0.0,
            this.coordinates?.getOrNull(1)?.toDoubleOrNull() ?: 0.0
        ),
        areaNo = this.areaNo ?: 0,
        address = this.address ?: ""

    )
}

fun List<ResponseStoreOwnerDto.Data?>?.toStoreOwnerModelList(): List<StoreOwnerModel> {
    return this?.filterNotNull()?.map { it.toStoreOwnerModel() } ?: emptyList()
}