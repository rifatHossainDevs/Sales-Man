package com.wevx.dealershipmanagement.payment

import android.annotation.SuppressLint
import androidx.navigation.fragment.findNavController
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.receipt.ReceiptFragment
import com.wevx.dealershipmanagement.SharedData
import com.wevx.dealershipmanagement.base.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentPaymentBinding
import com.wevx.dealershipmanagement.local_database.LocalDatabase.products
import com.wevx.dealershipmanagement.models.Products
import com.wevx.dealershipmanagement.recyclerView.ProductCartAdapter

class PaymentFragment : BaseFragment<FragmentPaymentBinding>(FragmentPaymentBinding::inflate) {
    private lateinit var adapter: ProductCartAdapter

    override fun setAllClickListener() {
        allSelectedProducts()
        binding.btnPayment.setOnClickListener {
            SharedData.productList = products

            findNavController().navigate(R.id.action_paymentFragment_to_receiptFragment)
        }

    }

    override fun allObserver() {

    }

    @SuppressLint("SetTextI18n")
    fun allSelectedProducts(){

        adapter= ProductCartAdapter(products)
        binding.recyclerProducts.adapter = adapter

        val total = products.sumOf { it.subtotal }
        binding.tvTotal.text = "Total: %.2f".format(total)
    }

}