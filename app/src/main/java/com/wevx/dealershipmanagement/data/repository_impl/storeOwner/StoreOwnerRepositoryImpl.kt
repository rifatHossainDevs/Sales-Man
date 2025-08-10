package com.wevx.dealershipmanagement.data.repository_impl.storeOwner

import com.wevx.dealershipmanagement.data.dto.getStoreOwnerByAreaDTO.ResponseStoreOwnerDto
import com.wevx.dealershipmanagement.data.dto.getStoreOwnerBySubDisDto.ResponseGetStoreOwnerBySubDistrictDTO
import com.wevx.dealershipmanagement.data.remote.storeOwner.StoreOwnerApiService
import com.wevx.dealershipmanagement.domain.repository.storeOwner.StoreOwnerRepository
import jakarta.inject.Inject
import retrofit2.Response

class StoreOwnerRepositoryImpl @Inject constructor(
    private val storeOwnerApiService: StoreOwnerApiService
): StoreOwnerRepository {

    override suspend fun getStoreOwnerByArea(areaId: Int): Response<ResponseStoreOwnerDto> {
        return storeOwnerApiService.getStoreOwnerByArea(areaId)

    }

    override suspend fun getStoreOwnerBySubDistrict(disId: Int): Response<ResponseGetStoreOwnerBySubDistrictDTO> {
        return storeOwnerApiService.getStoreOwnerBySubDis(disId)
    }

    override suspend fun getStoreOwnerId(id: String): Response<ResponseGetStoreOwnerBySubDistrictDTO> {
        return storeOwnerApiService.getStoreOwnerById(id)

    }

}