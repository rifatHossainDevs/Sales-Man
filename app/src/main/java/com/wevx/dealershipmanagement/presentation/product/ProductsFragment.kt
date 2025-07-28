package com.wevx.dealershipmanagement.presentation.product

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.utils.SharedData
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentProductsBinding
import com.wevx.dealershipmanagement.databinding.ProductCartBottomSheetBinding
import com.wevx.dealershipmanagement.utils.LocalDatabase.products
import com.wevx.dealershipmanagement.domain.models.CartItem
import com.wevx.dealershipmanagement.presentation.adapter.ProductAdapter
import com.wevx.dealershipmanagement.presentation.adapter.ProductCartAdapter

class ProductsFragment : BaseFragment<FragmentProductsBinding>(FragmentProductsBinding::inflate) {

    private var selectedCategory: String = ""
    private lateinit var bottomSheetBinding: ProductCartBottomSheetBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var adapter: ProductCartAdapter
    private lateinit var productAdapter: ProductAdapter

    lateinit var selectedItems: List<CartItem>
    lateinit var cartItems: List<CartItem>

    override fun setAllClickListener() {

        setProductRecyclerView()
        bottomSheetClickListener()
        spinnerClickListener()

    }

    private fun setProductRecyclerView() {
        binding.productsRecyclerView.setHasFixedSize(true)
        cartItems = products.map { CartItem(it, 0.0) }
        productAdapter = ProductAdapter(cartItems, object : ProductAdapter.HandleClickListener {
            @SuppressLint("SetTextI18n")
            override fun onQuantityChangedListener() {

            }

        })
        binding.productsRecyclerView.adapter = productAdapter
    }

    override fun allObserver() {

    }

    @SuppressLint("SetTextI18n")
    private fun bottomSheetClickListener() {
        bottomSheetBinding = ProductCartBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(requireContext())




        binding.btnContinue.setOnClickListener {
            selectedItems = cartItems.filter { it.purchaseQuantity > 0 }
            if (selectedItems.isEmpty()) {
                Toast.makeText(requireContext(), "No item is selected", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            SharedData.selectedProductList = selectedItems

            adapter = ProductCartAdapter(selectedItems)
            bottomSheetBinding.recyclerProducts.adapter = adapter

            val total = cartItems.sumOf { it.subtotal }
            bottomSheetBinding.tvTotal.text = "Total: %.2f".format(total)

            bottomSheetDialog.apply {
                setContentView(bottomSheetBinding.root)
                setCancelable(true)
                show()
            }
        }

        bottomSheetBinding.btnClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetBinding.btnConfirm.setOnClickListener {
            bottomSheetDialog.dismiss()
            findNavController().navigate(R.id.action_productsFragment_to_paymentFragment)
        }
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