package com.wevx.dealershipmanagement.data.remote.product

import com.wevx.dealershipmanagement.data.dto.categoryDTO.ResponseCategoryDTO
import com.wevx.dealershipmanagement.data.dto.districtDto.ResponseDisDTO
import com.wevx.dealershipmanagement.data.dto.productById.ResponseProductByIdDTO
import com.wevx.dealershipmanagement.data.dto.productDto.ResponseProductDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {

    @GET("categories/get-all-categories")
    suspend fun getCategory(): Response<ResponseCategoryDTO>

    @GET("products/get-all-products")
    suspend fun getAllProduct(): Response<ResponseProductDTO>

    @GET("products/get-product-by-category/{categoryId}")
    suspend fun getProductByCategory(@Path("categoryId") categoryId: String): Response<ResponseProductDTO>

    @GET("products/get-product/{productId}")
    suspend fun getProductById(@Path("productId") productId: String): Response<ResponseProductByIdDTO>

}