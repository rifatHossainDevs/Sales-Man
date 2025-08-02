package com.wevx.dealershipmanagement.domain.repository.auth

import com.wevx.dealershipmanagement.data.dto.changePasswordDTO.RequestChangePasswordDto
import com.wevx.dealershipmanagement.data.dto.changePasswordDTO.ResponseChangePasswordDTO
import com.wevx.dealershipmanagement.data.dto.loginDto.RequestLogin
import com.wevx.dealershipmanagement.data.dto.loginDto.ResponseLoginDTO
import com.wevx.dealershipmanagement.data.dto.profileDTO.ResponseProfileDto
import com.wevx.dealershipmanagement.data.dto.registrationDto.RequestRegistrationDto
import com.wevx.dealershipmanagement.data.dto.registrationDto.ResponseRegistrationDTO
import retrofit2.Response

interface AuthRepository {

    suspend fun login(requestLogin: RequestLogin): Response<ResponseLoginDTO>

    suspend fun registration(requestRegistration: RequestRegistrationDto): Response<ResponseRegistrationDTO>

    suspend fun getProfile(): Response<ResponseProfileDto>

    suspend fun changePassword(requestChangePassword: RequestChangePasswordDto): Response<ResponseChangePasswordDTO>


}