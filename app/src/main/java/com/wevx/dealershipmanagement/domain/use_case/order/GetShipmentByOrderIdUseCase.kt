package com.wevx.dealershipmanagement.domain.use_case.order

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.shipmentDto.ResponseGetShipmentByOrderDTO
import com.wevx.dealershipmanagement.domain.repository.order.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetShipmentByOrderIdUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(id: String): Flow<Resource<ResponseGetShipmentByOrderDTO?>> = flow {
        try {
            emit(Resource.Loading())

            val response = orderRepository.getShipmentByOrderId(id = id)

            if (response.isSuccessful) {
                val data = response.body()
                emit(Resource.Success(data = data))
            } else {
                emit(Resource.Error("get shipment failed: ${response.message()}"))
            }

        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }

}