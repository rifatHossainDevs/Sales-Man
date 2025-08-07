package com.wevx.dealershipmanagement.presentation.payment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.navigation.fragment.findNavController
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.utils.SharedData
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentPaymentBinding
import com.wevx.dealershipmanagement.utils.LocalDatabase.products
import com.wevx.dealershipmanagement.domain.models.CartItem
import com.wevx.dealershipmanagement.presentation.adapter.ProductCartAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentFragment : BaseFragment<FragmentPaymentBinding>(FragmentPaymentBinding::inflate) {
    private lateinit var adapter: ProductCartAdapter
    lateinit var selectedItems: List<CartItem>
    private lateinit var selectedDate: String

    override fun setAllClickListener() {
        allSelectedProducts()
        buttonClickListener()

    }

    override fun allObserver() {

    }

    private fun buttonClickListener() {
        binding.btnPayment.setOnClickListener {
            if (selectedDate.isEmpty()){
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, 1) // tomorrow
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                selectedDate = sdf.format(calendar.time)
            }

            findNavController().navigate(R.id.action_paymentFragment_to_receiptFragment)
        }
        binding.etExpectedShipmentDate.setOnClickListener {
            showDatePicker()
        }
    }


    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val sdf =
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            calendar.set(selectedYear, selectedMonth, selectedDay)
            selectedDate = sdf.format(calendar.time)
            binding.etExpectedShipmentDate.setText(selectedDate)
        }, year, month, day).show()
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