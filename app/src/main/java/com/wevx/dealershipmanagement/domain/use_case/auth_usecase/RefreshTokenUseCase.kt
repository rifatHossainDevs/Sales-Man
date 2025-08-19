package com.wevx.dealershipmanagement.domain.use_case.auth_usecase

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.authDto.RequestRefreshToken
import com.wevx.dealershipmanagement.data.dto.authDto.refreshTokenDto.ResponseRefreshTokenDTO
import com.wevx.dealershipmanagement.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(request: RequestRefreshToken): Flow<Resource<ResponseRefreshTokenDTO>> = flow {
        try {
            emit(Resource.Loading())
            val response = authRepository.refreshToken(request)

            if (response.isSuccessful && response.body() != null) {
                val responseBody = response.body()!!
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Error("Error: ${response.message()}"))
            }

        } catch (e: Exception) {
            emit(Resource.Error("Exception: ${e.message}"))
        }
    }
}