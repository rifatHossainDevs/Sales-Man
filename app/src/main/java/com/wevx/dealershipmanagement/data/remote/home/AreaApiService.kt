package com.wevx.dealershipmanagement.data.remote.home

import com.wevx.dealershipmanagement.data.dto.homeDto.areaDTO.ResponseAreaDTO
import com.wevx.dealershipmanagement.data.dto.homeDto.districtDto.ResponseDisDTO
import com.wevx.dealershipmanagement.data.dto.homeDto.subDistrictDto.ResponseSubDisDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AreaApiService {

    @GET("districts/get-district-by-div/{divId}")
    suspend fun getDistrict(@Path("divId") divId: Int): Response<ResponseDisDTO>

    @GET("sub-districts/get-subDistrict-by-dis/{disId}")
    suspend fun getSubDistrict(@Path("disId") disId: Int): Response<ResponseSubDisDTO>

    @GET("areas/get-area-by-subdis/{subDisId}")
    suspend fun getArea(@Path("subDisId") subDisId: Int): Response<ResponseAreaDTO>

}