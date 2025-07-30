package com.wevx.dealershipmanagement.domain.repository.home

import com.wevx.dealershipmanagement.data.dto.areaDTO.ResponseAreaDTO
import com.wevx.dealershipmanagement.data.dto.districtDto.ResponseDisDTO
import com.wevx.dealershipmanagement.data.dto.subDistrictDto.ResponseSubDisDTO
import retrofit2.Response

interface AreaRepository {

    suspend fun getDistrict(divId: Int): Response<ResponseDisDTO>
    suspend fun getSubDistrict(disId: Int): Response<ResponseSubDisDTO>
    suspend fun getArea(subDisId: Int): Response<ResponseAreaDTO>


}