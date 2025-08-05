package com.wevx.dealershipmanagement.domain.repository.storeOwner

import com.wevx.dealershipmanagement.data.dto.categoryDTO.ResponseCategoryDTO
import com.wevx.dealershipmanagement.data.dto.getStoreOwnerDTO.ResponseStoreOwnerDto
import com.wevx.dealershipmanagement.data.dto.productDto.ResponseProductDTO
import retrofit2.Response

interface StoreOwnerRepository {

    suspend fun getStoreOwnerByArea(areaId: Int): Response<ResponseStoreOwnerDto>



}