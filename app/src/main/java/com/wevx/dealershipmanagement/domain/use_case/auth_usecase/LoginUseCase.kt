package com.wevx.dealershipmanagement.domain.use_case.auth_usecase

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.authDto.loginDto.RequestLogin
import com.wevx.dealershipmanagement.data.dto.authDto.loginDto.toUserModel
import com.wevx.dealershipmanagement.domain.models.LoginModel
import com.wevx.dealershipmanagement.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(requestLogin: RequestLogin): Flow<Resource<LoginModel>> = flow {
        try {
            emit(Resource.Loading())
            val response = authRepository.login(requestLogin)

            if (response.isSuccessful) {
                val data = response.body()?.data?.toUserModel()
                if (data != null) {
                    emit(Resource.Success(data))
                } else {
                    emit(Resource.Error("Login failed: No data received"))
                }
            } else {
                emit(Resource.Error("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage}"))
        }
    }
}