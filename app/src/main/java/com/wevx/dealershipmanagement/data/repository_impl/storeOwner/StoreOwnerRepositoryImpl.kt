package com.wevx.dealershipmanagement.data.repository_impl.storeOwner

import com.wevx.dealershipmanagement.data.dto.categoryDTO.ResponseCategoryDTO
import com.wevx.dealershipmanagement.data.dto.getStoreOwnerDTO.ResponseStoreOwnerDto
import com.wevx.dealershipmanagement.data.dto.productDto.ResponseProductDTO
import com.wevx.dealershipmanagement.data.remote.product.ProductApiService
import com.wevx.dealershipmanagement.data.remote.storeOwner.StoreOwnerApiService
import com.wevx.dealershipmanagement.domain.repository.product.ProductRepository
import com.wevx.dealershipmanagement.domain.repository.storeOwner.StoreOwnerRepository
import jakarta.inject.Inject
import retrofit2.Response

class StoreOwnerRepositoryImpl @Inject constructor(
    private val storeOwnerApiService: StoreOwnerApiService
): StoreOwnerRepository {

    override suspend fun getStoreOwnerByArea(areaId: Int): Response<ResponseStoreOwnerDto> {
        return storeOwnerApiService.getStoreOwnerByArea(areaId)

    }

}