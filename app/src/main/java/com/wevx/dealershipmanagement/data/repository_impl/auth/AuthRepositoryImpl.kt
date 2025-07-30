package com.wevx.dealershipmanagement.data.repository_impl.auth

import com.wevx.dealershipmanagement.data.dto.loginDto.RequestLogin
import com.wevx.dealershipmanagement.data.dto.loginDto.ResponseLoginDTO
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

}