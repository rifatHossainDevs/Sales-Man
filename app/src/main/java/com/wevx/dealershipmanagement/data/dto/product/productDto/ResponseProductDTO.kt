package com.wevx.dealershipmanagement.data.dto.product.productDto

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.wevx.dealershipmanagement.domain.models.ProductModel
import kotlin.String

@Keep
@Serializable
data class ResponseProductDTO(
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
        @SerializedName("brandName")
        val brandName: String? = null,
        @SerializedName("categoryId")
        val categoryId: String? = null,
        @SerializedName("createdAt")
        val createdAt: String? = null,
        @SerializedName("description")
        val description: String? = null,
        @SerializedName("_id")
        val id: String? = null,
        @SerializedName("imageUrl")
        val imageUrl: List<String?>? = null,
        @SerializedName("isActive")
        val isActive: Boolean? = null,
        @SerializedName("offerPrice")
        val offerPrice: Double? = null,
        @SerializedName("price")
        val price: Double? = null,
        @SerializedName("productName")
        val productName: String? = null,
        @SerializedName("sku")
        val sku: String? = null,
        @SerializedName("stockQuantity")
        val stockQuantity: Double? = null,
        @SerializedName("supplierID")
        val supplierID: String? = null,
        @SerializedName("unit")
        val unit: String? = null,
        @SerializedName("updatedAt")
        val updatedAt: String? = null,
        @SerializedName("__v")
        val v: Int? = null
    )
}

fun ResponseProductDTO.Data.toProductModel(): ProductModel {
    return ProductModel(
        productId = this.id ?: "",
        productName = this.productName ?: "",
        brandName = this.brandName ?: "",
        description = this.description ?: "",
        price = this.price ?: 0.0,
        unit = this.unit ?: "",
        offerPrice = this.offerPrice ?: 0.0,
        stockQuantity = this.stockQuantity ?: 0.0,
        categoryId = this.categoryId ?: "",
        imageUrl = this.imageUrl?.firstOrNull { !it.isNullOrBlank() } ?: "",
        isActive = this.isActive ?: true
    )
}

fun List<ResponseProductDTO.Data?>?.toProductModelList(): List<ProductModel> {
    return this?.filterNotNull()?.map { it.toProductModel() } ?: emptyList()
}
