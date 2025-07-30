package com.wevx.dealershipmanagement.data.remote.home

import com.wevx.dealershipmanagement.data.dto.districtDto.ResponseDisDTO
import com.wevx.dealershipmanagement.data.dto.loginDto.RequestLogin
import com.wevx.dealershipmanagement.data.dto.loginDto.ResponseLoginDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeApiService {

    @GET("districts/get-district-by-div/{divId}")
    suspend fun getDistrict(@Path("divId") divId: Int): Response<ResponseDisDTO>

}