package com.wevx.dealershipmanagement.data.repository_impl.auth

import com.wevx.dealershipmanagement.data.dto.changePasswordDTO.RequestChangePasswordDto
import com.wevx.dealershipmanagement.data.dto.changePasswordDTO.ResponseChangePasswordDTO
import com.wevx.dealershipmanagement.data.dto.loginDto.RequestLogin
import com.wevx.dealershipmanagement.data.dto.loginDto.ResponseLoginDTO
import com.wevx.dealershipmanagement.data.dto.profileDTO.ResponseProfileDto
import com.wevx.dealershipmanagement.data.dto.registrationDto.RequestRegistrationDto
import com.wevx.dealershipmanagement.data.dto.registrationDto.ResponseRegistrationDTO
import com.wevx.dealershipmanagement.data.remote.auth.AuthApiService
import com.wevx.dealershipmanagement.domain.repository.auth.AuthRepository
import jakarta.inject.Inject
import retrofit2.Response

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService
): AuthRepository {

    override suspend fun login(requestLogin: RequestLogin): Response<ResponseLoginDTO> {
        return authApiService.login(requestLogin)
    }

    override suspend fun registration(requestRegistration: RequestRegistrationDto): Response<ResponseRegistrationDTO> {
        return authApiService.registration(requestRegistration)
    }

    override suspend fun getProfile(): Response<ResponseProfileDto> {
        return authApiService.getProfile()
    }

    override suspend fun changePassword(requestChangePassword: RequestChangePasswordDto): Response<ResponseChangePasswordDTO> {
        return authApiService.changePassword(requestChangePassword)
    }

}