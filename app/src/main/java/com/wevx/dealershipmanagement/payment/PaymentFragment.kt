package com.wevx.dealershipmanagement.payment

import android.annotation.SuppressLint
import androidx.navigation.fragment.findNavController
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.receipt.ReceiptFragment
import com.wevx.dealershipmanagement.SharedData
import com.wevx.dealershipmanagement.base.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentPaymentBinding
import com.wevx.dealershipmanagement.models.Products
import com.wevx.dealershipmanagement.recyclerView.ProductCartAdapter

class PaymentFragment : BaseFragment<FragmentPaymentBinding>(FragmentPaymentBinding::inflate) {
    private lateinit var adapter: ProductCartAdapter
    val products = listOf<Products>(
        Products("1", "Banana", 10.0, 2.0, "pcs", "Fruit"),
        Products("2", "Papaya", 30.0, 1.0, "ltr", "Dairy"),
        Products("3", "Bread", 20.0, 1.0, "pcs", "Bakery")
    )
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
        val products = listOf(
            Products("1", "Banana", 10.00, 10.00, "pcs", "Fruit"),
            Products("2", "Apple", 5.00, 25.00, "pcs", "Fruit"),
            Products("3", "Milk", 2.00, 30.00, "L", "Dairy"),
            Products("4", "Bread", 1.00, 20.00, "pcs", "Bakery"),
            Products("5", "Rice", 5.00, 200.00, "kg", "Grains"),
            Products("6", "Eggs", 12.00, 120.00, "pcs", "Poultry")
        )

        adapter= ProductCartAdapter(products)
        binding.recyclerProducts.adapter = adapter

        val total = products.sumOf { it.subtotal }
        binding.tvTotal.text = "Total: %.2f".format(total)
    }

}