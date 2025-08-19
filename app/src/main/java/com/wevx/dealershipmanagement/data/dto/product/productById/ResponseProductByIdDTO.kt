package com.wevx.dealershipmanagement.data.dto.product.productById

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
@Serializable
data class ResponseProductByIdDTO(
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
        val offerPrice: Int? = null,
        @SerializedName("price")
        val price: Int? = null,
        @SerializedName("productName")
        val productName: String? = null,
        @SerializedName("sku")
        val sku: String? = null,
        @SerializedName("stockQuantity")
        val stockQuantity: Int? = null,
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