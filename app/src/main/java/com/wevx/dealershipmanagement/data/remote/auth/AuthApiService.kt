package com.wevx.dealershipmanagement.data.remote.auth

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
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

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
    suspend fun refreshToken(@Body request: RequestRefreshToken): Response<ResponseRefreshTokenDTO>

    @Multipart
    @PATCH("users/update-avatar")
    suspend fun changeProfileImage(
        @Header("Authorization") token: String,
        @Part avatar: MultipartBody.Part
    ): Response<ResponseChangeProfileImage>

}
