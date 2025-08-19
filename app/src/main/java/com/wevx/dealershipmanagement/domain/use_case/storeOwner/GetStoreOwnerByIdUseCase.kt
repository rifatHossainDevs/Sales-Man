package com.wevx.dealershipmanagement.domain.use_case.storeOwner

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.store.getStoreById.toStoreOwnerById
import com.wevx.dealershipmanagement.domain.models.StoreOwnerByIdModel
import com.wevx.dealershipmanagement.domain.repository.storeOwner.StoreOwnerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetStoreOwnerByIdUseCase @Inject constructor(
    private val storeOwnerRepository: StoreOwnerRepository
) {
    operator fun invoke(id: String): Flow<Resource<StoreOwnerByIdModel>> = flow {
        try {
            emit(Resource.Loading())

            val response = storeOwnerRepository.getStoreOwnerId(id)

            if (response.isSuccessful) {
                val storeOwner = response.body()?.data?.toStoreOwnerById()
                if (storeOwner != null) {
                    emit(Resource.Success(storeOwner))
                } else {
                    emit(Resource.Error("No store owner found for the given ID"))
                }
            } else {
                emit(Resource.Error("Store Owner fetch failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }
}
