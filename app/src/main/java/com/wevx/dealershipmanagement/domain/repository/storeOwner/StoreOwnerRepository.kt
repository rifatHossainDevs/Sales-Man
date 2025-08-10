package com.wevx.dealershipmanagement.domain.repository.storeOwner

import com.wevx.dealershipmanagement.data.dto.getStoreById.ResponseGetStoreById
import com.wevx.dealershipmanagement.data.dto.getStoreOwnerByAreaDTO.ResponseStoreOwnerDto
import com.wevx.dealershipmanagement.data.dto.getStoreOwnerBySubDisDto.ResponseGetStoreOwnerBySubDistrictDTO
import retrofit2.Response

interface StoreOwnerRepository {

    suspend fun getStoreOwnerByArea(areaId: Int): Response<ResponseStoreOwnerDto>

    suspend fun getStoreOwnerBySubDistrict(disId: Int): Response<ResponseGetStoreOwnerBySubDistrictDTO>
    suspend fun getStoreOwnerId(id: String): Response<ResponseGetStoreById>



}