package com.wevx.dealershipmanagement.domain.use_case.home_usecase

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.districtDto.toDistrictModelList
import com.wevx.dealershipmanagement.domain.models.DistrictModel
import com.wevx.dealershipmanagement.domain.repository.home.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDistrictUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(divId: Int): Flow<Resource<List<DistrictModel>>> = flow {
        try {
            emit(Resource.Loading())

            val response = homeRepository.getDistrict(divId)

            if (response.isSuccessful) {
                val body = response.body()
                val responseList = body?.data ?: emptyList()
                val data = responseList.toDistrictModelList()

                emit(Resource.Success(data = data))
            } else {
                emit(Resource.Error("District fetch failed: ${response.message()}"))
            }

        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }

}
