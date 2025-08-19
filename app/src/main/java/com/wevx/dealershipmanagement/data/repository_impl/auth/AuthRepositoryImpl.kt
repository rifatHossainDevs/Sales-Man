package com.wevx.dealershipmanagement.data.repository_impl.auth

import com.wevx.dealershipmanagement.data.dto.authDto.RequestRefreshToken
import com.wevx.dealershipmanagement.data.dto.authDto.ResponseChangeProfileImage
import com.wevx.dealershipmanagement.data.dto.authDto.changePasswordDTO.RequestChangePasswordDto
import com.wevx.dealershipmanagement.data.dto.authDto.changePasswordDTO.ResponseChangePasswordDTO
import com.wevx.dealershipmanagement.data.dto.authDto.loginDto.RequestLogin
import com.wevx.dealershipmanagement.data.dto.authDto.loginDto.ResponseLoginDTO
import com.wevx.dealershipmanagement.data.dto.authDto.logoutDTO.ResponseLogoutDto
import com.wevx.dealershipmanagement.data.dto.authDto.profileDTO.ResponseProfileDto
import com.wevx.dealershipmanagement.data.dto.authDto.refreshTokenDto.ResponseRefreshTokenDTO
import com.wevx.dealershipmanagement.data.dto.authDto.registrationDto.RequestRegistrationDto
import com.wevx.dealershipmanagement.data.dto.authDto.registrationDto.ResponseRegistrationDTO
import com.wevx.dealershipmanagement.data.dto.authDto.updateProfileDto.RequestUpdateProfile
import com.wevx.dealershipmanagement.data.dto.authDto.updateProfileDto.ResponseUpdateProfileDto
import com.wevx.dealershipmanagement.data.remote.auth.AuthApiService
import com.wevx.dealershipmanagement.domain.repository.auth.AuthRepository
import com.wevx.dealershipmanagement.utils.toImagePart
import jakarta.inject.Inject
import retrofit2.Response
import java.io.File

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService
): AuthRepository {

    override suspend fun login(requestLogin: RequestLogin): Response<ResponseLoginDTO> {
        return authApiService.login(requestLogin)
    }

    override suspend fun registration(requestRegistration: RequestRegistrationDto): Response<ResponseRegistrationDTO> {
        return authApiService.registration(requestRegistration)
    }

    override suspend fun getProfile(token: String): Response<ResponseProfileDto> {
        return authApiService.getProfile(token)
    }

    override suspend fun changePassword(
        requestChangePassword: RequestChangePasswordDto,
        token: String
    ): Response<ResponseChangePasswordDTO> {
        return authApiService.changePassword(token, requestChangePassword)
    }

    override suspend fun logout(token: String): Response<ResponseLogoutDto> {
        return authApiService.logout(token)

    }

    override suspend fun refreshToken(requestRefreshToken: RequestRefreshToken): Response<ResponseRefreshTokenDTO> {
        return authApiService.refreshToken(requestRefreshToken)
    }

    override suspend fun updateProfile(
        requestUpdateProfile: RequestUpdateProfile,
        token: String
    ): Response<ResponseUpdateProfileDto> {
        return authApiService.updateProfile(token, requestUpdateProfile)

    }

    override suspend fun updateProfileImage(
        avatarFile: File,
        token: String
    ): Response<ResponseChangeProfileImage> {
        return authApiService.changeProfileImage(token = token, avatarFile.toImagePart("avatar"))
    }


}