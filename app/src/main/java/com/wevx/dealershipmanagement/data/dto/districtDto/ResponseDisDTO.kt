package com.wevx.dealershipmanagement.data.dto.districtDto


import androidx.annotation.Keep
import com.wevx.dealershipmanagement.domain.models.DistrictModel
import kotlinx.serialization.SerialName

@Keep
data class ResponseDisDTO(
    @SerialName("data")
    val `data`: List<Data?>? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("statusCode")
    val statusCode: Int? = null,
    @SerialName("success")
    val success: Boolean? = null
) {
    @Keep
    data class Data(
        @SerialName("createdAt")
        val createdAt: String? = null,
        @SerialName("disName")
        val disName: String? = null,
        @SerialName("disNo")
        val disNo: Int? = null,
        @SerialName("divNo")
        val divNo: Int? = null,
        @SerialName("_id")
        val id: String? = null,
        @SerialName("updatedAt")
        val updatedAt: String? = null,
        @SerialName("__v")
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