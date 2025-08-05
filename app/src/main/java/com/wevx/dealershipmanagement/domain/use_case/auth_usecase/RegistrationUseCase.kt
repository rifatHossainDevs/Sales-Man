package com.wevx.dealershipmanagement.domain.use_case.auth_usecase

import android.util.Log
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.registrationDto.RequestRegistrationDto
import com.wevx.dealershipmanagement.data.dto.registrationDto.toRegistrationModel
import com.wevx.dealershipmanagement.domain.models.RegistrationModel
import com.wevx.dealershipmanagement.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegistrationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(requestRegistration: RequestRegistrationDto): Flow<Resource<RegistrationModel>> = flow {
        try {
            emit(Resource.Loading())
            val response = authRepository.registration(requestRegistration)

            if (response.isSuccessful) {
                val data = response.body()?.data?.toRegistrationModel()
                if (data != null) {
                    emit(Resource.Success(data))
                } else {
                    emit(Resource.Error("Registration failed: No data received"))
                }
            } else {
                emit(Resource.Error("Registration failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage}"))
        }
    }
}