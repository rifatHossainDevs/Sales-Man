package com.wevx.dealershipmanagement.data.dto.homeDto.districtDto


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.wevx.dealershipmanagement.domain.models.DistrictModel

@Keep
data class ResponseDisDTO(
    @SerializedName("data")
    val `data`: List<Data?>? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("statusCode")
    val statusCode: Int? = null,
    @SerializedName("success")
    val success: Boolean? = null
) {
    @Keep
    data class Data(
        @SerializedName("createdAt")
        val createdAt: String? = null,
        @SerializedName("disName")
        val disName: String? = null,
        @SerializedName("disNo")
        val disNo: Int? = null,
        @SerializedName("divNo")
        val divNo: Int? = null,
        @SerializedName("_id")
        val id: String? = null,
        @SerializedName("updatedAt")
        val updatedAt: String? = null,
        @SerializedName("__v")
        val v: Int? = null
    )
}

fun ResponseDisDTO.Data.toDistrictModel(): DistrictModel {
    return DistrictModel(
        id = this.id ?: "",
        disName = this.disName ?: "",
        disNo = this.disNo ?: 0,
        divNo = this.divNo ?: 0
    )
}

fun List<ResponseDisDTO.Data?>?.toDistrictModelList(): List<DistrictModel> {
    return this?.filterNotNull()?.map { it.toDistrictModel() } ?: emptyList()
}