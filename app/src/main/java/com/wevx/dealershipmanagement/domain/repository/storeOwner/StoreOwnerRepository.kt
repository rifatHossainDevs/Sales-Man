package com.wevx.dealershipmanagement.domain.repository.storeOwner

import com.wevx.dealershipmanagement.data.dto.store.createStoreDTO.ResponseCreateStoreDTO
import com.wevx.dealershipmanagement.data.dto.store.getStoreById.ResponseGetStoreById
import com.wevx.dealershipmanagement.data.dto.homeDto.getStoreOwnerByAreaDTO.ResponseStoreOwnerDto
import com.wevx.dealershipmanagement.data.dto.homeDto.getStoreOwnerBySubDisDto.ResponseGetStoreOwnerBySubDistrictDTO
import retrofit2.Response
import java.io.File

interface StoreOwnerRepository {

    suspend fun getStoreOwnerByArea(areaId: Int): Response<ResponseStoreOwnerDto>

    suspend fun getStoreOwnerBySubDistrict(disId: Int): Response<ResponseGetStoreOwnerBySubDistrictDTO>
    suspend fun getStoreOwnerId(id: String): Response<ResponseGetStoreById>


    suspend fun createStore(
        userId: String,
        storeName: String,
        storePictureFile: File,
        coordinate1: String,
        coordinate2: String,
        areaNo: String,
        address: String,
        storeOwnerName: String,
        phone: String,
        avatarFile: File,
        subDisNo: String,
        token : String
    ): Response<ResponseCreateStoreDTO>


}