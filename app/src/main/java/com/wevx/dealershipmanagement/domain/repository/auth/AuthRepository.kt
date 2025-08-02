package com.wevx.dealershipmanagement.domain.repository.auth

import com.wevx.dealershipmanagement.data.dto.loginDto.RequestLogin
import com.wevx.dealershipmanagement.data.dto.loginDto.ResponseLoginDTO
import com.wevx.dealershipmanagement.data.dto.registrationDto.RequestRegistration
import com.wevx.dealershipmanagement.data.dto.registrationDto.ResponseRegistrationDTO
import retrofit2.Response

interface AuthRepository {

    suspend fun login(requestLogin: RequestLogin): Response<ResponseLoginDTO>

    suspend fun registration(requestRegistration: RequestRegistration): Response<ResponseRegistrationDTO>


}