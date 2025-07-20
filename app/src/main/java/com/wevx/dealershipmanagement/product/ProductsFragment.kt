package com.wevx.dealershipmanagement.product

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.base.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentProductsBinding
import com.wevx.dealershipmanagement.databinding.ProductCartBottomSheetBinding
import com.wevx.dealershipmanagement.models.Products
import com.wevx.dealershipmanagement.recyclerView.ProductCartAdapter

class ProductsFragment : BaseFragment<FragmentProductsBinding>(FragmentProductsBinding::inflate) {

    private var selectedCategory: String = ""

    private lateinit var bottomSheetBinding: ProductCartBottomSheetBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var adapter: ProductCartAdapter

    override fun setAllClickListener() {
        bottomSheetBinding = ProductCartBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetClickListener()
        spinnerClickListener()
    }

    @SuppressLint("SetTextI18n")
    private fun bottomSheetClickListener() {
        val products = listOf(
            Products("1", "Banana", 10.00, 10.00, "pcs", "Fruit"),
            Products("2", "Apple", 5.00, 25.00, "pcs", "Fruit"),
            Products("3", "Milk", 2.00, 30.00, "L", "Dairy"),
            Products("4", "Bread", 1.00, 20.00, "pcs", "Bakery"),
            Products("5", "Rice", 5.00, 200.00, "kg", "Grains"),
            Products("6", "Eggs", 12.00, 120.00, "pcs", "Poultry")
        )

        bottomSheetBinding.recyclerProducts.layoutManager = LinearLayoutManager(requireContext())

         adapter= ProductCartAdapter(products)
        bottomSheetBinding.recyclerProducts.adapter = adapter

        val total = products.sumOf { it.subtotal }
        bottomSheetBinding.tvTotal.text = "Total: %.2f".format(total)

        bottomSheetDialog.apply {
            setContentView(bottomSheetBinding.root)
            setCancelable(true)
        }

        binding.btnContinue.setOnClickListener {
            bottomSheetDialog.show()
        }

        bottomSheetBinding.btnClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetBinding.btnConfirm.setOnClickListener {
            bottomSheetDialog.dismiss()
            findNavController().navigate(R.id.action_productsFragment_to_paymentFragment)
        }
    }

    override fun allObserver() {

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun spinnerClickListener() {
        val categories = listOf("Select Category", "Electronics", "Groceries", "Clothing", "Books")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerCategory.adapter = adapter

        // Animate dropdown icon when touched
        binding.spinnerCategory.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                binding.categoryDropdownIcon.animate().rotation(180f).setDuration(200).start()
            }
            false
        }

        // Handle selection and reset icon
        binding.spinnerCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    selectedCategory = categories[position]
                    binding.categoryDropdownIcon.animate().rotation(0f).setDuration(200).start()
                    // You can use selectedCategory wherever you need
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }



}