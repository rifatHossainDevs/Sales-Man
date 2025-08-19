package com.wevx.dealershipmanagement.domain.repository.auth

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
import retrofit2.Response
import java.io.File

interface AuthRepository {

    suspend fun login(requestLogin: RequestLogin): Response<ResponseLoginDTO>

    suspend fun registration(requestRegistration: RequestRegistrationDto): Response<ResponseRegistrationDTO>

    suspend fun getProfile(token: String): Response<ResponseProfileDto>

    suspend fun changePassword(
        requestChangePassword: RequestChangePasswordDto,
        token: String
    ): Response<ResponseChangePasswordDTO>

    suspend fun logout(token: String): Response<ResponseLogoutDto>

    suspend fun refreshToken(requestRefreshToken: RequestRefreshToken): Response<ResponseRefreshTokenDTO>

    suspend fun updateProfile(
        requestUpdateProfile: RequestUpdateProfile,
        token: String
    ): Response<ResponseUpdateProfileDto>

    suspend fun updateProfileImage(
        avatarFile: File,
        token: String
    ): Response<ResponseChangeProfileImage>

}