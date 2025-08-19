package com.wevx.dealershipmanagement.data.remote.storeOwner

import com.wevx.dealershipmanagement.data.dto.store.createStoreDTO.ResponseCreateStoreDTO
import com.wevx.dealershipmanagement.data.dto.store.getStoreById.ResponseGetStoreById
import com.wevx.dealershipmanagement.data.dto.homeDto.getStoreOwnerByAreaDTO.ResponseStoreOwnerDto
import com.wevx.dealershipmanagement.data.dto.homeDto.getStoreOwnerBySubDisDto.ResponseGetStoreOwnerBySubDistrictDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface StoreOwnerApiService {

    @GET("stores/get-store-by-area/{areaId}")
    suspend fun getStoreOwnerByArea(@Path("areaId") areaId: Int): Response<ResponseStoreOwnerDto>

    @GET("stores/get-store-by-subdis/{disId}")
    suspend fun getStoreOwnerBySubDis(@Path("disId") disId: Int): Response<ResponseGetStoreOwnerBySubDistrictDTO>

    @GET("stores/get-store-by-id/{id}")
    suspend fun getStoreOwnerById(@Path("id") id: String): Response<ResponseGetStoreById>


    @Multipart
    @POST("stores/create-store")
    suspend fun createStore(
        @Part("userId") userId: RequestBody,
        @Part("storeName") storeName: RequestBody,
        @Part storePictures: MultipartBody.Part,
        @Part("coordinates") coordinate1: RequestBody,
        @Part("coordinates") coordinate2: RequestBody,
        @Part("areaNo") areaNo: RequestBody,
        @Part("address") address: RequestBody,
        @Part("storeOwnerName") storeOwnerName: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part avatar: MultipartBody.Part,
        @Part("subDisNo") subDisNo: RequestBody,
        @Header("Authorization") token: String
    ): Response<ResponseCreateStoreDTO>

}