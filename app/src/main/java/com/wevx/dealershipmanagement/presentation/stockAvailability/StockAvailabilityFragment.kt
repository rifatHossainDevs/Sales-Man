package com.wevx.dealershipmanagement.presentation.stockAvailability

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentStockAvailabilityBinding
import com.wevx.dealershipmanagement.presentation.adapter.ProductAdapter
import com.wevx.dealershipmanagement.presentation.adapter.StockAvailabilityAdapter
import com.wevx.dealershipmanagement.presentation.product.getAllProduct.AllProductViewModel
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StockAvailabilityFragment : BaseFragment<FragmentStockAvailabilityBinding>(
    FragmentStockAvailabilityBinding::inflate
) {
    private val allProductViewModel: AllProductViewModel by viewModels()
    lateinit var allProductAdapter: StockAvailabilityAdapter
    override fun setAllClickListener() {
        allProductViewModel.getAllProduct()
    }

    override fun allObserver() {
        allProductViewModel.allProductState.collectInLifecycle(viewLifecycleOwner){ allProductDataState->
            if (allProductDataState.loading) return@collectInLifecycle
            allProductDataState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
            allProductDataState.data?.let { allProductList->
                if (!allProductList.isEmpty()){
                    allProductAdapter = StockAvailabilityAdapter(allProductList)
                    binding.rvAllProduct.adapter = allProductAdapter
                }else{
                    binding.rvAllProduct.visibility = View.GONE
                    binding.tvNoDataFound.visibility = View.VISIBLE
                }


            }
        }
    }

}