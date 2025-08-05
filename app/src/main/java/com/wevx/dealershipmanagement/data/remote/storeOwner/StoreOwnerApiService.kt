package com.wevx.dealershipmanagement.data.remote.storeOwner

import com.wevx.dealershipmanagement.data.dto.categoryDTO.ResponseCategoryDTO
import com.wevx.dealershipmanagement.data.dto.districtDto.ResponseDisDTO
import com.wevx.dealershipmanagement.data.dto.getStoreOwnerDTO.ResponseStoreOwnerDto
import com.wevx.dealershipmanagement.data.dto.productDto.ResponseProductDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StoreOwnerApiService {

    @GET("stores/get-store-by-area/{areaId}")
    suspend fun getStoreOwnerByArea(@Path("areaId") areaId: Int): Response<ResponseStoreOwnerDto>

}