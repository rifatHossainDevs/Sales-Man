package com.wevx.dealershipmanagement.domain.use_case.order

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.order.updateOrder.RequestUpdateOrder
import com.wevx.dealershipmanagement.data.dto.order.updateOrder.ResponseUpdateOrderDTO
import com.wevx.dealershipmanagement.domain.repository.order.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(
        id: String,
        requestUpdateOrder: RequestUpdateOrder
    ): Flow<Resource<ResponseUpdateOrderDTO?>> = flow {
        try {
            emit(Resource.Loading())

            val response = orderRepository.updateOrder(
                id = id,
                requestUpdateOrder = requestUpdateOrder
            )

            if (response.isSuccessful) {
                val data = response.body()
                emit(Resource.Success(data = data))
            } else {
                emit(Resource.Error("Update Order failed: ${response.message()}"))
            }

        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }

}