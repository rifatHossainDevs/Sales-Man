package com.wevx.dealershipmanagement.domain.use_case.auth_usecase

import android.util.Log
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.loginDto.RequestLogin
import com.wevx.dealershipmanagement.data.dto.loginDto.toUserModel
import com.wevx.dealershipmanagement.data.dto.updateProfileDto.RequestUpdateProfile
import com.wevx.dealershipmanagement.data.dto.updateProfileDto.toUpdateProfileModel
import com.wevx.dealershipmanagement.domain.models.LoginModel
import com.wevx.dealershipmanagement.domain.models.UpdateProfileModel
import com.wevx.dealershipmanagement.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        requestUpdateProfile: RequestUpdateProfile,
        token: String
    ): Flow<Resource<UpdateProfileModel>> =
        flow {
            try {
                emit(Resource.Loading())
                val response = authRepository.updateProfile(requestUpdateProfile, token)

                if (response.isSuccessful) {
                    val data = response.body()?.toUpdateProfileModel()
                    if (data != null) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Error("Update Profile failed: No data received"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    emit(Resource.Error("Update Profile failed: ${response.code()} - ${errorBody ?: "No error body"}"))
                    Log.d("updateProfile", "invoke: code=${response.code()} body=$errorBody")
                }
            } catch (e: Exception) {
                emit(Resource.Error("An error occurred: ${e.localizedMessage}"))
            }
        }
}