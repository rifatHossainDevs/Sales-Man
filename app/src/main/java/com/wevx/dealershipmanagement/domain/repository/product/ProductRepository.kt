package com.wevx.dealershipmanagement.domain.repository.product

import com.wevx.dealershipmanagement.data.dto.responseCategoryDTO.ResponseCategoryDTO
import retrofit2.Response

interface ProductRepository {

    suspend fun getCategory(): Response<ResponseCategoryDTO>


}