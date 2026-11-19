package com.wevx.dealershipmanagement.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wevx.dealershipmanagement.R
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItem[position]
        val maxStock = cartItem.product.stockQuantity.coerceAtLeast(0.0)
        val formattedPrice = if (cartItem.product.price % 1.0 == 0.0) {
            cartItem.product.price.toLong().toString()
        } else {
            "%.2f".format(cartItem.product.price)
        }
        val formattedStock = if (maxStock % 1.0 == 0.0) {
            maxStock.toInt().toString()
        } else {
            maxStock.toString()
        }

        holder.binding.apply {
            tvProductName.text = cartItem.product.productName
            tvBrandName.text = cartItem.product.brandName
            tvPrice.text = "Price: ৳$formattedPrice"

            if (etQuantity.tag is TextWatcher) {
                etQuantity.removeTextChangedListener(etQuantity.tag as TextWatcher)
            }
            Glide.with(holder.itemView.context)
                .load(cartItem.product.imageUrl.replace("http://", "https://"))
                .placeholder(R.drawable.ic_product_24)
                .error(R.drawable.ic_product_24)
                .into(ivProduct)

            if (!etQuantity.hasFocus()) {
                if (cartItem.purchaseQuantity % 1.0 == 0.0)
                    etQuantity.setText(cartItem.purchaseQuantity.toInt().toString())
                else
                    etQuantity.setText(cartItem.purchaseQuantity.toString())
                etQuantity.setSelection(etQuantity.text.length)
            }

            tvStock.text = "Stock: $formattedStock ${cartItem.product.unit}"
            tvSubtotal.text = "Sub total: ৳%.2f".format(cartItem.subtotal)

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
                        if (entered > maxStock) {
                            cartItem.purchaseQuantity = maxStock
                            val maxStr = if (maxStock % 1.0 == 0.0) maxStock.toInt().toString() else maxStock.toString()
                            etQuantity.setText(maxStr)
                            etQuantity.setSelection(etQuantity.text.length)
                            Toast.makeText(
                                holder.itemView.context,
                                "Cannot exceed available stock ($formattedStock ${cartItem.product.unit})",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            cartItem.purchaseQuantity = entered
                        }
                    } else if (s.isNullOrEmpty()) {
                        cartItem.purchaseQuantity = 0.0
                    }
                    tvSubtotal.text = "Sub total: ৳%.2f".format(cartItem.subtotal)
                    listener.onQuantityChangedListener()

                    isEditing = false
                    etQuantity.isCursorVisible = true
                }
            }
            etQuantity.addTextChangedListener(watcher)
            etQuantity.tag = watcher

            ivIncrement.setOnClickListener {
                if (cartItem.purchaseQuantity + 1 <= maxStock) {
                    cartItem.purchaseQuantity += 1
                    etQuantity.setText(cartItem.purchaseQuantity.toInt().toString())
                    etQuantity.setSelection(etQuantity.text.length)
                    tvSubtotal.text = "Sub total: ৳%.2f".format(cartItem.subtotal)
                    listener.onQuantityChangedListener()
                } else {
                    Toast.makeText(
                        holder.itemView.context,
                        "Cannot exceed available stock ($formattedStock ${cartItem.product.unit})",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            ivDecrement.setOnClickListener {
                if (cartItem.purchaseQuantity > 0) {
                    cartItem.purchaseQuantity -= 1
                    etQuantity.setText(cartItem.purchaseQuantity.toInt().toString())
                    etQuantity.setSelection(etQuantity.text.length)
                    tvSubtotal.text = "Sub total: ৳%.2f".format(cartItem.subtotal)
                    listener.onQuantityChangedListener()
                }
            }

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
