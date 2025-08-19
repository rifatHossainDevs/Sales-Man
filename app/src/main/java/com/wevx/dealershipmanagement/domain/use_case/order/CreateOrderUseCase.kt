package com.wevx.dealershipmanagement.domain.use_case.order

import android.util.Log
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.order.createOrderDto.RequestCreateOrderDTO
import com.wevx.dealershipmanagement.data.dto.order.createOrderDto.ResponseCreateOrderDTO
import com.wevx.dealershipmanagement.domain.repository.order.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(requestCreateOrderDTO: RequestCreateOrderDTO, token: String): Flow<Resource<ResponseCreateOrderDTO>> =
        flow {
            try {
                emit(Resource.Loading())

                val response = orderRepository.createOrder(requestCreateOrderDTO, token)

                if (response.isSuccessful) {
                    val data = response.body()

                    if (data != null) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Error("Create order failed: No data received"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    emit(Resource.Error("Create Store Failed: $errorBody"))
                    Log.d("errorPayment", "invoke: $errorBody")
                }
            } catch (e: Exception) {
                emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
            }
        }

}
