package com.wevx.dealershipmanagement.domain.repository.product

import com.wevx.dealershipmanagement.data.dto.product.categoryDTO.ResponseCategoryDTO
import com.wevx.dealershipmanagement.data.dto.product.productById.ResponseProductByIdDTO
import com.wevx.dealershipmanagement.data.dto.product.productDto.ResponseProductDTO
import retrofit2.Response

interface ProductRepository {

    suspend fun getCategory(): Response<ResponseCategoryDTO>

    suspend fun getAllProduct(): Response<ResponseProductDTO>

    suspend fun getProductByCategory(categoryId: String): Response<ResponseProductDTO>

    suspend fun getProductById(productId: String): Response<ResponseProductByIdDTO>

}