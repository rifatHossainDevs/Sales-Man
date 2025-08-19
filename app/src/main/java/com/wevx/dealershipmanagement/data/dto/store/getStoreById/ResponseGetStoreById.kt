package com.wevx.dealershipmanagement.data.dto.store.getStoreById


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.wevx.dealershipmanagement.domain.models.Coordinates
import com.wevx.dealershipmanagement.domain.models.StoreOwnerByIdModel

@Keep
@Serializable
data class ResponseGetStoreById(
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

fun ResponseGetStoreById.Data.toStoreOwnerById():  StoreOwnerByIdModel{
    return StoreOwnerByIdModel(
        id = this.id ?: "",
        userId = this.userId ?: "",
        storeOwnerName = this.storeOwnerName ?: "",
        phone = this.phone ?: "",
        storeOwnerAvatar = this.avatar ?: "",
        storeName = this.storeName ?: "",
        coordinates = Coordinates(
            this.coordinates?.getOrNull(0)?.toDoubleOrNull() ?: 0.0,
            this.coordinates?.getOrNull(1)?.toDoubleOrNull() ?: 0.0
        ),
        areaNo = this.areaNo ?: 0,
        address = this.address ?: ""

    )
}
