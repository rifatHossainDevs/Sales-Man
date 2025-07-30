package com.wevx.dealershipmanagement.domain.repository.home

import com.wevx.dealershipmanagement.data.dto.districtDto.ResponseDisDTO
import retrofit2.Response

interface HomeRepository {

    suspend fun getDistrict(divId: Int): Response<ResponseDisDTO>


}