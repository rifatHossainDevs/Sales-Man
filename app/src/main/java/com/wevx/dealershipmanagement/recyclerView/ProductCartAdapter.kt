package com.wevx.dealershipmanagement.recyclerView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wevx.dealershipmanagement.databinding.ItemLayoutProductCartBinding
import com.wevx.dealershipmanagement.models.CartItem

class ProductCartAdapter(val cartItem: List<CartItem>) :
    RecyclerView.Adapter<ProductCartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemLayoutProductCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        cartItem[position].let { cartItem ->
            holder.binding.apply {
                tvName.text = cartItem.product.productName
                tvQuantity.text = "${cartItem.purchaseQuantity} ${cartItem.product.productUnit}"
                tvSubtotal.text = "%.2f".format(cartItem.subtotal)

            }
        }

    }

    override fun getItemCount(): Int = cartItem.size

    class ViewHolder(val binding: ItemLayoutProductCartBinding) :
        RecyclerView.ViewHolder(binding.root)
}