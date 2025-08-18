package com.wevx.dealershipmanagement.domain.use_case.order

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.shipmentDto.RequestUpdateShipment
import com.wevx.dealershipmanagement.data.dto.shipmentDto.ResponseUpdateShipmentDTO
import com.wevx.dealershipmanagement.domain.repository.order.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateShipmentUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(
        id: String,
        requestUpdateShipment: RequestUpdateShipment
    ): Flow<Resource<ResponseUpdateShipmentDTO?>> = flow {
        try {
            emit(Resource.Loading())

            val response = orderRepository.updateShipment(id = id, requestUpdateShipment = requestUpdateShipment)

            if (response.isSuccessful) {
                val data = response.body()
                emit(Resource.Success(data = data))
            } else {
                emit(Resource.Error("Update shipment failed: ${response.message()}"))
            }

        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }

}