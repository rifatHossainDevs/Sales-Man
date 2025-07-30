package com.wevx.dealershipmanagement.data.repository_impl.product

import com.wevx.dealershipmanagement.data.dto.responseAreaDTO.ResponseAreaDTO
import com.wevx.dealershipmanagement.data.dto.districtDto.ResponseDisDTO
import com.wevx.dealershipmanagement.data.dto.responseCategoryDTO.ResponseCategoryDTO
import com.wevx.dealershipmanagement.data.dto.subDistrictDto.ResponseSubDisDTO
import com.wevx.dealershipmanagement.data.remote.home.HomeApiService
import com.wevx.dealershipmanagement.data.remote.product.ProductApiService
import com.wevx.dealershipmanagement.domain.repository.home.HomeRepository
import com.wevx.dealershipmanagement.domain.repository.product.ProductRepository
import jakarta.inject.Inject
import retrofit2.Response

class ProductRepositoryImpl @Inject constructor(
    private val productApiService: ProductApiService
): ProductRepository {
    
    override suspend fun getCategory(): Response<ResponseCategoryDTO> {
        return productApiService.getCategory()

    }


}