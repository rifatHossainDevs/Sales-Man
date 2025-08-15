package com.wevx.dealershipmanagement.presentation.product

import ProductViewModel
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.utils.SharedData
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentProductsBinding
import com.wevx.dealershipmanagement.databinding.ProductCartBottomSheetBinding
import com.wevx.dealershipmanagement.domain.models.CartItem
import com.wevx.dealershipmanagement.domain.models.CategoryModel
import com.wevx.dealershipmanagement.presentation.adapter.ProductAdapter
import com.wevx.dealershipmanagement.presentation.adapter.ProductCartAdapter
import com.wevx.dealershipmanagement.presentation.product.getAllProduct.AllProductViewModel
import com.wevx.dealershipmanagement.presentation.product.getCategory.CategoryViewModel
import com.wevx.dealershipmanagement.presentation.product.productByCategory.ProductByCategoryViewModel
import com.wevx.dealershipmanagement.presentation.storeOwnerDetails.StoreOwnerDetailsFragmentArgs
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class ProductsFragment : BaseFragment<FragmentProductsBinding>(FragmentProductsBinding::inflate) {

    private var selectedCategoryId: String = ""
    private lateinit var bottomSheetBinding: ProductCartBottomSheetBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var adapter: ProductCartAdapter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var cartItems: List<CartItem>
    private lateinit var selectedItems: List<CartItem>
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val allProductViewModel: AllProductViewModel by viewModels()
    private val productByCategoryViewModel: ProductByCategoryViewModel by viewModels()
    private val productViewModel: ProductViewModel by activityViewModels()
    private val args: ProductsFragmentArgs by navArgs()
    private lateinit var customerId: String
    private val cartItemMap = mutableMapOf<String, CartItem>()

    override fun setAllClickListener() {
        customerId = args.id
        bottomSheetClickListener()
        categoryViewModel.getCategory()
        allProductViewModel.getAllProduct()

    }

    override fun allObserver() {
        categoryObserver()
        allProductObserver()
        productByCategoryObserver()
    }

    private fun allProductObserver() {
        allProductViewModel.allProductState.collectInLifecycle(viewLifecycleOwner) { productState ->
            if (productState.loading){
                loading.show()
                return@collectInLifecycle
            }

            productState.error?.let {
                loading.dismiss()
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

            productState.data?.let { allProductList ->
                loading.dismiss()
                productViewModel.allProducts = allProductList
                selectedCategoryId = ""
                setProductRecyclerView()
            }
        }
    }

    private fun productByCategoryObserver() {
        productByCategoryViewModel.productByCategoryState.collectInLifecycle(viewLifecycleOwner) { productState ->
            if (productState.loading){
                loading.show()
                return@collectInLifecycle
            }

            productState.error?.let {
                loading.dismiss()
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

            productState.data?.let { filteredProductList ->
                loading.dismiss()
                productViewModel.filteredProducts = filteredProductList
                setProductRecyclerView()
            }
        }
    }

    private fun categoryObserver() {
        categoryViewModel.categoryState.collectInLifecycle(viewLifecycleOwner) { categoryState ->
            if (categoryState.loading) return@collectInLifecycle

            categoryState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

            categoryState.data?.let { categoryList ->
                val categoryNames = mutableListOf("Select Category")
                categoryNames.addAll(categoryList.map { it.categoryName })

                spinnerClickListener(categoryNames, categoryList)
            }
        }
    }

    private fun setProductRecyclerView() {
        val currentProducts = if (selectedCategoryId.isEmpty()) {
            productViewModel.allProducts
        } else {
            productViewModel.filteredProducts
        }

        cartItems = currentProducts.map { product ->
            val existing = cartItemMap[product.productId]
            CartItem(product, existing?.purchaseQuantity ?: 0.0)
        }

        cartItems.forEach { cartItemMap[it.product.productId] = it }

        productAdapter = ProductAdapter(cartItems, object : ProductAdapter.HandleClickListener {
            override fun onQuantityChangedListener() {
                cartItems.forEach { cartItemMap[it.product.productId] = it }
            }
        })

        binding.productsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.productsRecyclerView.setHasFixedSize(true)
        binding.productsRecyclerView.adapter = productAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun bottomSheetClickListener() {
        bottomSheetBinding = ProductCartBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(requireContext())

        binding.btnContinue.setOnClickListener {
            selectedItems = cartItemMap.values.filter { it.purchaseQuantity > 0 }

            if (selectedItems.isEmpty()) {
                Toast.makeText(requireContext(), "No item is selected", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            SharedData.selectedProductList = selectedItems

            adapter = ProductCartAdapter(selectedItems)
            bottomSheetBinding.recyclerProducts.adapter = adapter

            val total = selectedItems.sumOf { it.subtotal }
            bottomSheetBinding.tvTotal.text = "Total: ৳%.2f".format(total)

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
            val action = ProductsFragmentDirections.actionProductsFragmentToPaymentFragment(customerId)
            findNavController().navigate(action)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun spinnerClickListener(
        categoryNames: List<String>,
        categoryList: List<CategoryModel>
    ) {
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, categoryNames
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerCategory.adapter = adapter

        binding.spinnerCategory.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                binding.categoryDropdownIcon.animate().rotation(180f).setDuration(200).start()
            }
            false
        }

        binding.spinnerCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    binding.categoryDropdownIcon.animate().rotation(0f).setDuration(200).start()

                    if (position > 0) {
                        val selectedModel = categoryList[position - 1]
                        selectedCategoryId = selectedModel.id
                        productByCategoryViewModel.getProductByCategory(selectedCategoryId)
                    } else {
                        selectedCategoryId = ""
                        setProductRecyclerView()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }
}
