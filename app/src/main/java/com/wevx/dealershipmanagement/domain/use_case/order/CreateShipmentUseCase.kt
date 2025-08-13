package com.wevx.dealershipmanagement.domain.use_case.order

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.shipmentDto.RequestShipmentDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.ResponseShipmentDTO
import com.wevx.dealershipmanagement.domain.repository.order.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateShipmentUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(requestShipmentDTO: RequestShipmentDTO, token: String): Flow<Resource<ResponseShipmentDTO>> =
        flow {
            try {
                emit(Resource.Loading())

                val response = orderRepository.createShipment(requestShipmentDTO, token)

                if (response.isSuccessful) {
                    val data = response.body()

                    if (data != null) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Error("Create shipment failed: No data received"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    emit(Resource.Error("Create shipment Failed: $errorBody"))
                }
            } catch (e: Exception) {
                emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
            }
        }

}
