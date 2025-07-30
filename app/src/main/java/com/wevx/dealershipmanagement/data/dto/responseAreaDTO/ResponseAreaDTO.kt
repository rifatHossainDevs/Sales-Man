package com.wevx.dealershipmanagement.data.dto.responseAreaDTO


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.wevx.dealershipmanagement.domain.models.AreaModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ResponseAreaDTO(
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
        @SerializedName("areaName")
        val areaName: String? = null,
        @SerializedName("areaNo")
        val areaNo: Int? = null,
        @SerializedName("createdAt")
        val createdAt: String? = null,
        @SerializedName("_id")
        val id: String? = null,
        @SerializedName("subdisNo")
        val subdisNo: Int? = null,
        @SerializedName("updatedAt")
        val updatedAt: String? = null,
        @SerializedName("__v")
        val v: Int? = null
    )
}

fun ResponseAreaDTO.Data.toAreaModel(): AreaModel {
    return AreaModel(
        id = this.id ?: "",
        areaName = this.areaName ?: "",
        areaNo = this.areaNo ?: 0,
        subDisNo = this.subdisNo ?: 0
    )
}

fun List<ResponseAreaDTO.Data?>?.toAreaModelList(): List<AreaModel> {
    return this?.filterNotNull()?.map { it.toAreaModel() } ?: emptyList()
}