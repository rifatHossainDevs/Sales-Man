package com.wevx.dealershipmanagement.data.repository_impl.order

import com.wevx.dealershipmanagement.data.createOrderDto.RequestCreateOrderDTO
import com.wevx.dealershipmanagement.data.createOrderDto.ResponseCreateOrderDTO
import com.wevx.dealershipmanagement.data.dto.categoryDTO.ResponseCategoryDTO
import com.wevx.dealershipmanagement.data.dto.pendingOrderDto.ResponsePendingOrderDTO
import com.wevx.dealershipmanagement.data.dto.productDto.ResponseProductDTO
import com.wevx.dealershipmanagement.data.remote.order.OrderApiService
import com.wevx.dealershipmanagement.data.remote.product.ProductApiService
import com.wevx.dealershipmanagement.domain.repository.order.OrderRepository
import com.wevx.dealershipmanagement.domain.repository.product.ProductRepository
import jakarta.inject.Inject
import retrofit2.Response

class OrderRepositoryImpl @Inject constructor(
    private val orderApiService: OrderApiService
): OrderRepository {
    override suspend fun getPendingOrdersByCustomer(customerId: String): Response<ResponsePendingOrderDTO> {
        return orderApiService.getPendingOrderByCustomer(customerId)
    }

    override suspend fun createOrder(requestCreateOrderDTO: RequestCreateOrderDTO, token: String): Response<ResponseCreateOrderDTO> {
        return orderApiService.createOrder(requestCreateOrderDTO, token)
    }


}