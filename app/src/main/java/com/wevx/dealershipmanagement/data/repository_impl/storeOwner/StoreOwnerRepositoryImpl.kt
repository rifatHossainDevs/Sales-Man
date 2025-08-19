package com.wevx.dealershipmanagement.data.repository_impl.storeOwner

import com.wevx.dealershipmanagement.data.dto.store.createStoreDTO.ResponseCreateStoreDTO
import com.wevx.dealershipmanagement.data.dto.store.getStoreById.ResponseGetStoreById
import com.wevx.dealershipmanagement.data.dto.homeDto.getStoreOwnerByAreaDTO.ResponseStoreOwnerDto
import com.wevx.dealershipmanagement.data.dto.homeDto.getStoreOwnerBySubDisDto.ResponseGetStoreOwnerBySubDistrictDTO
import com.wevx.dealershipmanagement.data.remote.storeOwner.StoreOwnerApiService
import com.wevx.dealershipmanagement.domain.repository.storeOwner.StoreOwnerRepository
import com.wevx.dealershipmanagement.utils.toImagePart
import com.wevx.dealershipmanagement.utils.toPart
import jakarta.inject.Inject
import retrofit2.Response
import java.io.File

class StoreOwnerRepositoryImpl @Inject constructor(
    private val storeOwnerApiService: StoreOwnerApiService
) : StoreOwnerRepository {

    override suspend fun getStoreOwnerByArea(areaId: Int): Response<ResponseStoreOwnerDto> {
        return storeOwnerApiService.getStoreOwnerByArea(areaId)

    }

    override suspend fun getStoreOwnerBySubDistrict(disId: Int): Response<ResponseGetStoreOwnerBySubDistrictDTO> {
        return storeOwnerApiService.getStoreOwnerBySubDis(disId)
    }

    override suspend fun getStoreOwnerId(id: String): Response<ResponseGetStoreById> {
        return storeOwnerApiService.getStoreOwnerById(id)


    }

    override suspend fun createStore(
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
        token: String
    ): Response<ResponseCreateStoreDTO> {
        return storeOwnerApiService.createStore(
            userId.toPart(),
            storeName.toPart(),
            storePictureFile.toImagePart("storePictures"),
            coordinate1.toPart(),
            coordinate2.toPart(),
            areaNo.toPart(),
            address.toPart(),
            storeOwnerName.toPart(),
            phone.toPart(),
            avatarFile.toImagePart("avatar"),
            subDisNo.toPart(),
            token
        )
    }


}