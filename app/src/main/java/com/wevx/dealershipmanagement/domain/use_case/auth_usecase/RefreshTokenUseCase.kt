package com.wevx.dealershipmanagement.domain.use_case.auth_usecase

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.refreshTokenDto.ResponseRefreshTokenDTO
import com.wevx.dealershipmanagement.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(token: String): Flow<Resource<ResponseRefreshTokenDTO>> = flow {
        try {
            emit(Resource.Loading())
            val response = authRepository.refreshToken(token)

            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    emit(Resource.Success(data))
                } else {
                    emit(Resource.Error("Get refresh token failed: No data received"))
                }
            } else {
                emit(Resource.Error("Get refresh token failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage}"))
        }
    }
}