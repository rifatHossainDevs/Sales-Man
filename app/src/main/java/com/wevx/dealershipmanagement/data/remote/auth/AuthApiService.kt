package com.wevx.dealershipmanagement.data.remote.auth

import com.wevx.dealershipmanagement.data.dto.loginDto.RequestLogin
import com.wevx.dealershipmanagement.data.dto.loginDto.ResponseLoginDTO
import com.wevx.dealershipmanagement.data.dto.registrationDto.RequestRegistration
import com.wevx.dealershipmanagement.data.dto.registrationDto.ResponseRegistrationDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("users/login")
    suspend fun login(@Body requestLogin: RequestLogin): Response<ResponseLoginDTO>

    @POST("users/register")
    suspend fun registration(@Body requestRegistration: RequestRegistration): Response<ResponseRegistrationDTO>




}