package com.wevx.dealershipmanagement.domain.use_case.storeOwner

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.getStoreOwnerByAreaDTO.toStoreOwnerModelList
import com.wevx.dealershipmanagement.data.dto.getStoreOwnerBySubDisDto.toStoreOwnerModelList
import com.wevx.dealershipmanagement.domain.models.StoreOwnerModel
import com.wevx.dealershipmanagement.domain.repository.storeOwner.StoreOwnerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetStoreOwnerByDistrictUseCase @Inject constructor(
    private val storeOwnerRepository: StoreOwnerRepository
) {
    operator fun invoke(disId: Int): Flow<Resource<List<StoreOwnerModel>>> = flow {
        try {
            emit(Resource.Loading())

            val response = storeOwnerRepository.getStoreOwnerBySubDistrict(disId)

            if (response.isSuccessful) {
                val body = response.body()
                val responseList = body?.data ?: emptyList()
                val data = responseList.toStoreOwnerModelList()

                emit(Resource.Success(data = data))
            } else {
                emit(Resource.Error("Store Owner by dis fetch failed: ${response.message()}"))
            }

        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }

}
