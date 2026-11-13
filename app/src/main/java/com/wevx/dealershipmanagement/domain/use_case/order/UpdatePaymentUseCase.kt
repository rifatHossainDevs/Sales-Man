package com.wevx.dealershipmanagement.domain.use_case.order

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.paymentDto.updatePayment.RequestUpdatePayment
import com.wevx.dealershipmanagement.data.dto.paymentDto.updatePayment.ResponseUpdatePaymentDTO
import com.wevx.dealershipmanagement.domain.repository.order.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdatePaymentUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(id: String, requestUpdatePayment: RequestUpdatePayment): Flow<Resource<ResponseUpdatePaymentDTO?>> = flow {
        try {
            emit(Resource.Loading())

            val response = orderRepository.updatePayment(id = id, requestUpdatePayment = requestUpdatePayment)

            if (response.isSuccessful) {
                emit(Resource.Success(data = response.body()))
            } else {
                val errorBody = response.errorBody()?.string()

                emit(
                    Resource.Error(
                        "HTTP ${response.code()}: ${errorBody ?: response.message()}"
                    )
                )
            }

        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }

}