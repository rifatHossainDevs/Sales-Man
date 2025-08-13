package com.wevx.dealershipmanagement.data.remote.auth

import com.wevx.dealershipmanagement.data.dto.changePasswordDTO.RequestChangePasswordDto
import com.wevx.dealershipmanagement.data.dto.changePasswordDTO.ResponseChangePasswordDTO
import com.wevx.dealershipmanagement.data.dto.loginDto.RequestLogin
import com.wevx.dealershipmanagement.data.dto.loginDto.ResponseLoginDTO
import com.wevx.dealershipmanagement.data.dto.logoutDTO.ResponseLogoutDto
import com.wevx.dealershipmanagement.data.dto.profileDTO.ResponseProfileDto
import com.wevx.dealershipmanagement.data.dto.refreshTokenDto.ResponseRefreshTokenDTO
import com.wevx.dealershipmanagement.data.dto.registrationDto.RequestRegistrationDto
import com.wevx.dealershipmanagement.data.dto.registrationDto.ResponseRegistrationDTO
import com.wevx.dealershipmanagement.data.dto.updateProfileDto.RequestUpdateProfile
import com.wevx.dealershipmanagement.data.dto.updateProfileDto.ResponseUpdateProfileDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApiService {

    @POST("users/login")
    suspend fun login(@Body requestLogin: RequestLogin): Response<ResponseLoginDTO>

    @POST("users/register")
    suspend fun registration(@Body requestRegistration: RequestRegistrationDto): Response<ResponseRegistrationDTO>

    @GET("users/current-user")
    suspend fun getProfile(@Header("Authorization") token: String): Response<ResponseProfileDto>

    @POST("users/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body requestChangePassword: RequestChangePasswordDto
    ): Response<ResponseChangePasswordDTO>

    @POST("users/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<ResponseLogoutDto>

    @PATCH("users/update-account")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body requestUpdateProfile: RequestUpdateProfile
    ): Response<ResponseUpdateProfileDto>

    @POST("users/refresh-token")
    suspend fun refreshToken(@Header("Authorization") token: String): Response<ResponseRefreshTokenDTO>

}
