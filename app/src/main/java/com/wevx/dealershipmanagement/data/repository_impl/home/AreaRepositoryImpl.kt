package com.wevx.dealershipmanagement.data.repository_impl.home

import com.wevx.dealershipmanagement.data.dto.areaDTO.ResponseAreaDTO
import com.wevx.dealershipmanagement.data.dto.districtDto.ResponseDisDTO
import com.wevx.dealershipmanagement.data.dto.subDistrictDto.ResponseSubDisDTO
import com.wevx.dealershipmanagement.data.remote.home.AreaApiService
import com.wevx.dealershipmanagement.domain.repository.home.AreaRepository
import jakarta.inject.Inject
import retrofit2.Response

class AreaRepositoryImpl @Inject constructor(
    private val areaApiService: AreaApiService
): AreaRepository {

    override suspend fun getDistrict(divId: Int): Response<ResponseDisDTO> {
        return areaApiService.getDistrict(divId)

    }

    override suspend fun getSubDistrict(disId: Int): Response<ResponseSubDisDTO> {
        return areaApiService.getSubDistrict(disId)
    }

    override suspend fun getArea(subDisId: Int): Response<ResponseAreaDTO> {
        return areaApiService.getArea(subDisId)
    }


}