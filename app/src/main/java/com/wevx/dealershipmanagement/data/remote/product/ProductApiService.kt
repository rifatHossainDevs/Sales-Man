package com.wevx.dealershipmanagement.data.remote.product

import com.wevx.dealershipmanagement.data.dto.responseCategoryDTO.ResponseCategoryDTO
import retrofit2.Response
import retrofit2.http.GET

interface ProductApiService {

    @GET("categories/get-all-categories")
    suspend fun getCategory(): Response<ResponseCategoryDTO>

}