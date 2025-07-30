package com.wevx.dealershipmanagement.domain.use_case.home_usecase

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.areaDTO.toAreaModelList
import com.wevx.dealershipmanagement.domain.models.AreaModel
import com.wevx.dealershipmanagement.domain.repository.home.AreaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAreaUseCase @Inject constructor(
    private val areaRepository: AreaRepository
) {
    operator fun invoke(subDisId: Int): Flow<Resource<List<AreaModel>>> = flow {
        try {
            emit(Resource.Loading())

            val response = areaRepository.getArea(subDisId)

            if (response.isSuccessful) {
                val body = response.body()
                val responseList = body?.data ?: emptyList()
                val data = responseList.toAreaModelList()

                emit(Resource.Success(data = data))
            } else {
                emit(Resource.Error("Area fetch failed: ${response.message()}"))
            }

        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }

}
