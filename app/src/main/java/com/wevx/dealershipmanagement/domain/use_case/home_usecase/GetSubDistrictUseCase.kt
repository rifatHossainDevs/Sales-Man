package com.wevx.dealershipmanagement.domain.use_case.home_usecase

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.subDistrictDto.toSubDistrictModelList
import com.wevx.dealershipmanagement.domain.models.SubDistrictModel
import com.wevx.dealershipmanagement.domain.repository.home.AreaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSubDistrictUseCase @Inject constructor(
    private val areaRepository: AreaRepository
) {
    operator fun invoke(disId: Int): Flow<Resource<List<SubDistrictModel>>> = flow {
        try {
            emit(Resource.Loading())

            val response = areaRepository.getSubDistrict(disId)

            if (response.isSuccessful) {
                val body = response.body()
                val responseList = body?.data ?: emptyList()
                val data = responseList.toSubDistrictModelList()

                emit(Resource.Success(data = data))
            } else {
                emit(Resource.Error("Sub District fetch failed: ${response.message()}"))
            }

        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }

}
