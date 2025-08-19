package com.wevx.dealershipmanagement.domain.use_case.order

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.order.todaysDelivery.toPendingOrderSellerModel
import com.wevx.dealershipmanagement.domain.models.PendingOrderSellerModel
import com.wevx.dealershipmanagement.domain.repository.order.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SellerPendingOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(sellerId: String, paymentStatus: String): Flow<Resource<List<PendingOrderSellerModel>>> = flow {
        try {
            emit(Resource.Loading())

            val response = orderRepository.getSellerPendingOrder(sellerId, paymentStatus)

            if (response.isSuccessful) {
                val body = response.body()
                val responseList = body?.data ?: emptyList()
                val data = responseList.toPendingOrderSellerModel()

                emit(Resource.Success(data = data))
            } else {
                emit(Resource.Error("Seller Pending Order fetch failed: ${response.message()}"))
            }

        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }

}
