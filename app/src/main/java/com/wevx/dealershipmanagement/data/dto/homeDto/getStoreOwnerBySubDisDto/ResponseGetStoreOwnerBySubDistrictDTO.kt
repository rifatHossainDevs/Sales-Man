package com.wevx.dealershipmanagement.data.dto.homeDto.getStoreOwnerBySubDisDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.wevx.dealershipmanagement.domain.models.StoreOwnerModel

@Keep
@Serializable
data class ResponseGetStoreOwnerBySubDistrictDTO(
    @SerialName("data")
    val `data`: List<Data?>? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("statusCode")
    val statusCode: Int? = null,
    @SerialName("success")
    val success: Boolean? = null
){
    @Keep
    @Serializable
    data class Data(
        @SerialName("address")
        val address: String? = null,
        @SerialName("areaNo")
        val areaNo: Int? = null,
        @SerialName("avatar")
        val avatar: String? = null,
        @SerialName("coordinates")
        val coordinates: List<String?>? = null,
        @SerialName("createdAt")
        val createdAt: String? = null,
        @SerialName("_id")
        val id: String? = null,
        @SerialName("phone")
        val phone: String? = null,
        @SerialName("storeName")
        val storeName: String? = null,
        @SerialName("storeOwnerName")
        val storeOwnerName: String? = null,
        @SerialName("storePictures")
        val storePictures: String? = null,
        @SerialName("subDisNo")
        val subDisNo: Int? = null,
        @SerialName("underCompanyId")
        val underCompanyId: String? = null,
        @SerialName("updatedAt")
        val updatedAt: String? = null,
        @SerialName("userId")
        val userId: String? = null,
        @SerialName("__v")
        val v: Int? = null
    )
}

fun ResponseGetStoreOwnerBySubDistrictDTO.Data.toStoreOwnerModel():  StoreOwnerModel{
    return StoreOwnerModel(
        id = this.id ?: "",
        userId = this.userId ?: "",
        storeOwnerName = this.storeOwnerName ?: "",
        phone = this.phone ?: "",
        storeOwnerAvatar = this.avatar ?: "",
        storeName = this.storeName ?: "",
        storeImg = this.storePictures ?: "",
        coordinates = coordinates,
        areaNo = this.areaNo ?: 0,
        address = this.address ?: ""

    )
}

fun List<ResponseGetStoreOwnerBySubDistrictDTO.Data?>?.toStoreOwnerModelList(): List<StoreOwnerModel> {
    return this?.filterNotNull()?.map { it.toStoreOwnerModel() } ?: emptyList()
}

