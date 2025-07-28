package com.wevx.dealershipmanagement.presentation.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wevx.dealershipmanagement.databinding.ItemLayoutProductBinding
import com.wevx.dealershipmanagement.domain.models.CartItem

class ProductAdapter(
    private val cartItem: List<CartItem>,
    private val listener: HandleClickListener
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    interface HandleClickListener {
        fun onQuantityChangedListener()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLayoutProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItem[position]
        holder.binding.apply {
            tvProductName.text = cartItem.product.productName
            tvBrandName.text = cartItem.product.brandName
            tvPrice.text = "Price: ${cartItem.product.productPricePerUnit}"
            ivProduct.load(cartItem.product.imageUrl)

            // Remove previous TextWatcher if any
            if (etQuantity.tag is TextWatcher) {
                etQuantity.removeTextChangedListener(etQuantity.tag as TextWatcher)
            }

            // Update EditText text only if not focused (to avoid cursor jumping)
            if (!etQuantity.hasFocus()) {
                if (cartItem.purchaseQuantity % 1.0 == 0.0)
                    etQuantity.setText(cartItem.purchaseQuantity.toInt().toString())
                else
                    etQuantity.setText(cartItem.purchaseQuantity.toString())
                etQuantity.setSelection(etQuantity.text.length)
            }

            tvSubtotal.text = "Sub total: %.2f".format(cartItem.subtotal)

            var isEditing = false
            val watcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (isEditing) return
                    isEditing = true

                    val entered = s.toString().toDoubleOrNull()
                    if (entered != null) {
                        cartItem.purchaseQuantity = entered
                    } else if (s.isNullOrEmpty()) {
                        // User cleared EditText - set qty = 0 but do NOT reset EditText text here
                        cartItem.purchaseQuantity = 0.0
                    }
                    tvSubtotal.text = "Sub total: %.2f".format(cartItem.subtotal)
                    listener.onQuantityChangedListener()

                    isEditing = false
                    etQuantity.isCursorVisible = true
                }
            }
            etQuantity.addTextChangedListener(watcher)
            etQuantity.tag = watcher

            ivIncrement.setOnClickListener {
                cartItem.purchaseQuantity += 1
                etQuantity.setText(cartItem.purchaseQuantity.toInt().toString())
                etQuantity.setSelection(etQuantity.text.length)
                tvSubtotal.text = "Sub total: %.2f".format(cartItem.subtotal)
                listener.onQuantityChangedListener()
            }

            ivDecrement.setOnClickListener {
                if (cartItem.purchaseQuantity > 0) {
                    cartItem.purchaseQuantity -= 1
                    etQuantity.setText(cartItem.purchaseQuantity.toInt().toString())
                    etQuantity.setSelection(etQuantity.text.length)
                    tvSubtotal.text = "Sub total: %.2f".format(cartItem.subtotal)
                    listener.onQuantityChangedListener()
                }
            }

            // Show cursor properly on click
            etQuantity.setOnClickListener {
                if (!etQuantity.hasFocus()) {
                    etQuantity.requestFocus()
                    etQuantity.isCursorVisible = true
                    val imm =
                        etQuantity.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(
                        etQuantity,
                        InputMethodManager.SHOW_IMPLICIT
                    )
                }
            }
        }
    }


    override fun getItemCount(): Int = cartItem.size

    class ViewHolder(val binding: ItemLayoutProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}
