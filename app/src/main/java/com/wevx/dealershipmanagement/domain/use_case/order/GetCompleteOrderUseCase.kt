package com.wevx.dealershipmanagement.domain.use_case.order

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.order.pendingAndCompleteOrderDto.toPendingAndCompleteOrderModelList
import com.wevx.dealershipmanagement.domain.models.PendingAndCompleteOrderModel
import com.wevx.dealershipmanagement.domain.repository.order.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCompleteOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(customerId: String, paymentStatus: String): Flow<Resource<List<PendingAndCompleteOrderModel>>> = flow {
        try {
            emit(Resource.Loading())

            val response = orderRepository.getCompleteOrderByCustomer(customerId,paymentStatus)

            if (response.isSuccessful) {
                val body = response.body()
                val responseList = body?.data ?: emptyList()
                val data = responseList.toPendingAndCompleteOrderModelList()

                emit(Resource.Success(data = data))
            } else {
                emit(Resource.Error("Complete fetch failed: ${response.message()}"))
            }

        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }

}
