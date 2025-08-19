package com.wevx.dealershipmanagement.domain.use_case.auth_usecase

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.authDto.changePasswordDTO.RequestChangePasswordDto
import com.wevx.dealershipmanagement.data.dto.authDto.changePasswordDTO.ResponseChangePasswordDTO
import com.wevx.dealershipmanagement.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(requestChangePassword: RequestChangePasswordDto, token: String): Flow<Resource<ResponseChangePasswordDTO>> = flow {
        try {
            emit(Resource.Loading())
            val response = authRepository.changePassword(requestChangePassword, token)

            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    emit(Resource.Success(data))
                } else {
                    emit(Resource.Error("Change Password failed"))
                }
            } else {
                emit(Resource.Error("Change Password failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage}"))
        }
    }
}