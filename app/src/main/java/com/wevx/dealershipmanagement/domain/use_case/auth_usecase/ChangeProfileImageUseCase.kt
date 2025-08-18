package com.wevx.dealershipmanagement.domain.use_case.auth_usecase

import android.util.Log
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.ResponseChangeProfileImage
import com.wevx.dealershipmanagement.data.dto.createStoreDTO.toCreateStoreModel
import com.wevx.dealershipmanagement.domain.models.CreateStoreModel
import com.wevx.dealershipmanagement.domain.repository.auth.AuthRepository
import com.wevx.dealershipmanagement.domain.repository.storeOwner.StoreOwnerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class ChangeProfileImageUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        avatarFile: File,
        token: String
    ): Flow<Resource<ResponseChangeProfileImage>> = flow {
        try {
            emit(Resource.Loading())

            val response = authRepository.updateProfileImage(
                avatarFile,
                token
            )

            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    emit(Resource.Success(data))
                } else {
                    emit(Resource.Error("Update profile image Failed : No data received"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                emit(Resource.Error("Update profile image Failed: $errorBody"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }
}
