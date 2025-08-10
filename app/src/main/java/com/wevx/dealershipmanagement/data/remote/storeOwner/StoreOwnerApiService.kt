package com.wevx.dealershipmanagement.data.remote.storeOwner

import com.wevx.dealershipmanagement.data.dto.getStoreOwnerByAreaDTO.ResponseStoreOwnerDto
import com.wevx.dealershipmanagement.data.dto.getStoreOwnerBySubDisDto.ResponseGetStoreOwnerBySubDistrictDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StoreOwnerApiService {

    @GET("stores/get-store-by-area/{areaId}")
    suspend fun getStoreOwnerByArea(@Path("areaId") areaId: Int): Response<ResponseStoreOwnerDto>

    @GET("stores/get-store-by-subdis/{disId}")
    suspend fun getStoreOwnerBySubDis(@Path("disId") disId: Int): Response<ResponseGetStoreOwnerBySubDistrictDTO>

    @GET("stores/get-store-by-id/{id}")
    suspend fun getStoreOwnerById(@Path("id") id: String): Response<ResponseGetStoreOwnerBySubDistrictDTO>
}