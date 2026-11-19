package com.wevx.dealershipmanagement.domain.use_case.order

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.order.todaysDelivery.ResponseTodaysDelivery
import com.wevx.dealershipmanagement.data.dto.order.todaysDelivery.toPendingOrderSellerModel
import com.wevx.dealershipmanagement.domain.models.PendingOrderSellerModel
import com.wevx.dealershipmanagement.domain.repository.order.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SellerPendingOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(
        sellerId: String,
        paymentStatus: String
    ): Flow<Resource<List<PendingOrderSellerModel>>> = flow {
        try {
            emit(Resource.Loading())

            val collectedOrders = LinkedHashMap<String, ResponseTodaysDelivery.Data>()

            // 1. Primary endpoint: salesman's pending delivery orders
            try {
                val response = orderRepository.getSellerPendingOrder(sellerId, paymentStatus)
                if (response.isSuccessful) {
                    response.body()?.data?.filterNotNull()?.forEach { item ->
                        if (!item.id.isNullOrBlank()) {
                            collectedOrders[item.id] = item
                        }
                    }
                }
            } catch (_: Exception) {
            }

            // 2. Also fetch all pending delivery orders across the salesman's stores (independent of expectedShipDate)
            try {
                val storesRes = orderRepository.getAllStoresForDelivery()
                if (storesRes.isSuccessful) {
                    val dataElement = storesRes.body()?.get("data")
                    val storesArray = when {
                        dataElement != null && dataElement.isJsonObject ->
                            dataElement.asJsonObject.getAsJsonArray("stores")
                        dataElement != null && dataElement.isJsonArray ->
                            dataElement.asJsonArray
                        else -> null
                    }
                    storesArray?.forEach { storeEl ->
                        if (storeEl.isJsonObject) {
                            val storeObj = storeEl.asJsonObject
                            val storeId = storeObj.get("_id")?.asString ?: ""
                            if (storeId.isNotBlank()) {
                                try {
                                    val custOrdersRes = orderRepository.getOrdersByCustomer(storeId)
                                    if (custOrdersRes.isSuccessful) {
                                        custOrdersRes.body()?.data?.filterNotNull()?.forEach { orderItem ->
                                            val matchesSeller = orderItem.salesmanId.isNullOrBlank() ||
                                                orderItem.salesmanId == sellerId
                                            if (matchesSeller && !orderItem.id.isNullOrBlank()) {
                                                collectedOrders[orderItem.id] = orderItem
                                            }
                                        }
                                    }
                                } catch (_: Exception) {
                                }
                            }
                        }
                    }
                }
            } catch (_: Exception) {
            }

            val finalModels = collectedOrders.values.toList().toPendingOrderSellerModel()
            emit(Resource.Success(data = finalModels))
        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }
}
