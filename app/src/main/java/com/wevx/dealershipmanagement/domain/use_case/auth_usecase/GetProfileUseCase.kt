package com.wevx.dealershipmanagement.domain.use_case.auth_usecase

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.profileDTO.toProfileModel
import com.wevx.dealershipmanagement.domain.models.ProfileModel
import com.wevx.dealershipmanagement.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<Resource<ProfileModel>> = flow {
        try {
            emit(Resource.Loading())
            val response = authRepository.getProfile()

            if (response.isSuccessful) {
                val data = response.body()?.data?.toProfileModel()
                if (data != null) {
                    emit(Resource.Success(data))
                } else {
                    emit(Resource.Error("Fetch Profile failed: No data received"))
                }
            } else {
                emit(Resource.Error("Fetch Profile failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage}"))
        }
    }
}