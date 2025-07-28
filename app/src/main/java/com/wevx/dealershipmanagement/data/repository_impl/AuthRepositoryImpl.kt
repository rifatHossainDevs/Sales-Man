package com.wevx.dealershipmanagement.data.repository_impl

import com.wevx.dealershipmanagement.data.dto.RequestLogin
import com.wevx.dealershipmanagement.data.dto.ResponseLoginDTO
import com.wevx.dealershipmanagement.data.remote.AuthApiService
import com.wevx.dealershipmanagement.domain.repository.AuthRepository
import jakarta.inject.Inject
import retrofit2.Response

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService
): AuthRepository {

    override suspend fun login(requestLogin: RequestLogin): Response<ResponseLoginDTO> {
        return authApiService.login(requestLogin)
    }

}