package com.wevx.dealershipmanagement.domain.use_case.product_usecase

import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.product.categoryDTO.toCategoryList
import com.wevx.dealershipmanagement.domain.models.CategoryModel
import com.wevx.dealershipmanagement.domain.repository.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(): Flow<Resource<List<CategoryModel>>> = flow {
        try {
            emit(Resource.Loading())

            val response = productRepository.getCategory()

            if (response.isSuccessful) {
                val body = response.body()
                val responseList = body?.data ?: emptyList()
                val data = responseList.toCategoryList()

                emit(Resource.Success(data = data))
            } else {
                emit(Resource.Error("Category fetch failed: ${response.message()}"))
            }

        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }

}
