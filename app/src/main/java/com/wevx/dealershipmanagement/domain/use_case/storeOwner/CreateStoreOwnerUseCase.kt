package com.wevx.dealershipmanagement.domain.use_case.storeOwner

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.store.createStoreDTO.toCreateStoreModel
import com.wevx.dealershipmanagement.domain.models.CreateStoreModel
import com.wevx.dealershipmanagement.domain.repository.storeOwner.StoreOwnerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class CreateStoreOwnerUseCase @Inject constructor(
    private val storeOwnerRepository: StoreOwnerRepository
) {
    operator fun invoke(
        userId: String,
        storeName: String,
        storePictureFile: File,
        coordinate1: String,
        coordinate2: String,
        areaNo: String,
        address: String,
        storeOwnerName: String,
        phone: String,
        avatarFile: File,
        subDisNo: String,
        token: String
    ): Flow<Resource<CreateStoreModel>> = flow {
        try {
            emit(Resource.Loading())

            val response = storeOwnerRepository.createStore(
                userId,
                storeName,
                storePictureFile,
                coordinate1,
                coordinate2,
                areaNo,
                address,
                storeOwnerName,
                phone,
                avatarFile,
                subDisNo,
                token
            )

            if (response.isSuccessful) {
                val data = response.body()?.toCreateStoreModel()
                if (data != null) {
                    emit(Resource.Success(data))
                } else {
                    emit(Resource.Error("Create Store Failed : No data received"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                emit(Resource.Error("Create Store Failed: $errorBody"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }
}
