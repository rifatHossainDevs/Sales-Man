package com.wevx.dealershipmanagement.domain.use_case.order

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.ResponseGetPaymentDTO
import com.wevx.dealershipmanagement.domain.repository.order.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPaymentByOrderIdUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(orderId: String): Flow<Resource<ResponseGetPaymentDTO?>> = flow {
        try {
            emit(Resource.Loading())

            val response = orderRepository.getPaymentByOrderID(orderId)

            if (response.isSuccessful) {
                val data = response.body()
                emit(Resource.Success(data = data))
            } else {
                emit(Resource.Error("Get Payment fetch failed: ${response.message()}"))
            }

        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }

}
