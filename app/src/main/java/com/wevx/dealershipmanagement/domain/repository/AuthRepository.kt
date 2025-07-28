package com.wevx.dealershipmanagement.domain.repository

import com.wevx.dealershipmanagement.data.dto.RequestLogin
import com.wevx.dealershipmanagement.data.dto.ResponseLoginDTO
import retrofit2.Response

interface AuthRepository {

    suspend fun login(requestLogin: RequestLogin): Response<ResponseLoginDTO>


}