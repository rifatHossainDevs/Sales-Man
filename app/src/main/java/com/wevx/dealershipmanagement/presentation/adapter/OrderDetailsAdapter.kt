package com.wevx.dealershipmanagement.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wevx.dealershipmanagement.databinding.ItemLayoutProductCartBinding
import com.wevx.dealershipmanagement.domain.models.OrderDetailsModel

class OrderDetailsAdapter(val productsList: List<OrderDetailsModel>) :
    RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {

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
        productsList[position].let { product ->
            val qtyStr = if (product.quantity % 1.0 == 0.0) {
                product.quantity.toInt().toString()
            } else {
                product.quantity.toString()
            }
            holder.binding.apply {
                tvName.text = product.name
                tvQuantity.text = "$qtyStr pcs"
                tvSubtotal.text = "৳%.2f".format(product.subtotal)
            }
        }
    }

    override fun getItemCount(): Int = productsList.size

    class ViewHolder(val binding: ItemLayoutProductCartBinding) :
        RecyclerView.ViewHolder(binding.root)
}
