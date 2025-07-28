package com.wevx.dealershipmanagement.presentation.payment

import android.annotation.SuppressLint
import androidx.navigation.fragment.findNavController
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.utils.SharedData
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentPaymentBinding
import com.wevx.dealershipmanagement.utils.LocalDatabase.products
import com.wevx.dealershipmanagement.domain.models.CartItem
import com.wevx.dealershipmanagement.presentation.adapter.ProductCartAdapter

class PaymentFragment : BaseFragment<FragmentPaymentBinding>(FragmentPaymentBinding::inflate) {
    private lateinit var adapter: ProductCartAdapter
    lateinit var selectedItems: List<CartItem>

    override fun setAllClickListener() {
        allSelectedProducts()
        buttonClickListener()


    }

    private fun buttonClickListener() {
        binding.btnPayment.setOnClickListener {

            findNavController().navigate(R.id.action_paymentFragment_to_receiptFragment)
        }
    }

    override fun allObserver() {

    }

    @SuppressLint("SetTextI18n")
    fun allSelectedProducts() {
        selectedItems = SharedData.selectedProductList

        adapter = ProductCartAdapter(selectedItems)
        binding.recyclerProducts.adapter = adapter

        val total = products.sumOf { it.subtotal }
        binding.tvTotal.text = "Total: %.2f".format(total)
    }

}