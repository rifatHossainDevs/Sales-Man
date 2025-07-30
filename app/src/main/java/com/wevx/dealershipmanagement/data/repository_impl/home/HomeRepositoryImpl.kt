package com.wevx.dealershipmanagement.data.repository_impl.home

import com.wevx.dealershipmanagement.data.dto.responseAreaDTO.ResponseAreaDTO
import com.wevx.dealershipmanagement.data.dto.districtDto.ResponseDisDTO
import com.wevx.dealershipmanagement.data.dto.subDistrictDto.ResponseSubDisDTO
import com.wevx.dealershipmanagement.data.remote.home.HomeApiService
import com.wevx.dealershipmanagement.domain.repository.home.HomeRepository
import jakarta.inject.Inject
import retrofit2.Response

class HomeRepositoryImpl @Inject constructor(
    private val homeApiService: HomeApiService
): HomeRepository {

    override suspend fun getDistrict(divId: Int): Response<ResponseDisDTO> {
        return homeApiService.getDistrict(divId)

    }

    override suspend fun getSubDistrict(disId: Int): Response<ResponseSubDisDTO> {
        return homeApiService.getSubDistrict(disId)
    }

    override suspend fun getArea(subDisId: Int): Response<ResponseAreaDTO> {
        return homeApiService.getArea(subDisId)
    }


}