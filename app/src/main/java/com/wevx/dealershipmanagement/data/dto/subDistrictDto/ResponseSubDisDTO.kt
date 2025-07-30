package com.wevx.dealershipmanagement.data.dto.subDistrictDto

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.wevx.dealershipmanagement.domain.models.SubDistrictModel

@Keep
@Serializable
data class ResponseSubDisDTO(
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
        @SerializedName("createdAt")
        val createdAt: String? = null,
        @SerializedName("disNo")
        val disNo: Int? = null,
        @SerializedName("_id")
        val id: String? = null,
        @SerializedName("subDisName")
        val subDisName: String? = null,
        @SerializedName("subDisNo")
        val subDisNo: Int? = null,
        @SerializedName("updatedAt")
        val updatedAt: String? = null,
        @SerializedName("__v")
        val v: Int? = null
    )
}

fun ResponseSubDisDTO.Data.toSubDistrictModel(): SubDistrictModel {
    return SubDistrictModel(
        id = this.id ?: "",
        subDisName = this.subDisName ?: "",
        subDisNo = this.subDisNo ?: 0,
        disNo = this.disNo ?: 0
    )
}

fun List<ResponseSubDisDTO.Data?>?.toSubDistrictModelList(): List<SubDistrictModel> {
    return this?.filterNotNull()?.map { it.toSubDistrictModel() } ?: emptyList()
}