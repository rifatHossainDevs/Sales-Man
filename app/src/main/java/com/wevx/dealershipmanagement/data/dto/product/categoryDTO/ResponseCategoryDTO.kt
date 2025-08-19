package com.wevx.dealershipmanagement.data.dto.product.categoryDTO

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.wevx.dealershipmanagement.domain.models.CategoryModel

@Keep
@Serializable
data class ResponseCategoryDTO(
    @SerializedName("data")
    val data: List<Data?>? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("statusCode")
    val statusCode: Int? = null,
    @SerializedName("success")
    val success: Boolean? = null
) {
    data class Data(
        @SerializedName("_id")
        val id: String? = null,
        @SerializedName("categoryName")
        val categoryName: String? = null,
        @SerializedName("description")
        val description: String? = null,
        @SerializedName("createdAt")
        val createdAt: String? = null,
        @SerializedName("updatedAt")
        val updatedAt: String? = null,
        @SerializedName("__v")
        val v: Int? = null
    )
}

fun ResponseCategoryDTO.Data.toCategory(): CategoryModel {
    return CategoryModel(
        id = this.id ?: "",
        categoryName = this.categoryName ?: "",
        description = this.description ?: ""
    )

}

fun List<ResponseCategoryDTO.Data?>?.toCategoryList(): List<CategoryModel> {
    return this?.filterNotNull()?.map { it.toCategory() } ?: emptyList()
}