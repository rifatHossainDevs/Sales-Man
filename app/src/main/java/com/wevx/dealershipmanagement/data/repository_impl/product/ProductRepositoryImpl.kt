package com.wevx.dealershipmanagement.data.repository_impl.product

import com.wevx.dealershipmanagement.data.dto.categoryDTO.ResponseCategoryDTO
import com.wevx.dealershipmanagement.data.dto.productById.ResponseProductByIdDTO
import com.wevx.dealershipmanagement.data.dto.productDto.ResponseProductDTO
import com.wevx.dealershipmanagement.data.remote.product.ProductApiService
import com.wevx.dealershipmanagement.domain.repository.product.ProductRepository
import jakarta.inject.Inject
import retrofit2.Response

class ProductRepositoryImpl @Inject constructor(
    private val productApiService: ProductApiService
): ProductRepository {
    
    override suspend fun getCategory(): Response<ResponseCategoryDTO> {
        return productApiService.getCategory()
    }

    override suspend fun getAllProduct(): Response<ResponseProductDTO> {
        return productApiService.getAllProduct()
    }

    override suspend fun getProductByCategory(categoryId: String): Response<ResponseProductDTO> {
        return productApiService.getProductByCategory(categoryId)
    }

    override suspend fun getProductById(productId: String): Response<ResponseProductByIdDTO> {
        return productApiService.getProductById(productId)

    }


}