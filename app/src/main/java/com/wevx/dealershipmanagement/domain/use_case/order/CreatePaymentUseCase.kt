package com.wevx.dealershipmanagement.domain.use_case.order

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.paymentDto.RequestPaymentDTO
import com.wevx.dealershipmanagement.data.dto.paymentDto.ResponsePaymentDTO
import com.wevx.dealershipmanagement.domain.repository.order.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreatePaymentUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(requestPaymentDTO: RequestPaymentDTO, token: String): Flow<Resource<ResponsePaymentDTO>> =
        flow {
            try {
                emit(Resource.Loading())

                val response = orderRepository.createPayment(requestPaymentDTO, token)

                if (response.isSuccessful) {
                    val data = response.body()

                    if (data != null) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Error("Create payment failed: No data received"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    emit(Resource.Error("Create payment Failed: $errorBody"))
                }
            } catch (e: Exception) {
                emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
            }
        }

}
