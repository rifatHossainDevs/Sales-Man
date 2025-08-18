package com.wevx.dealershipmanagement.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wevx.dealershipmanagement.data.dto.orderDetailsDTO.ResponseOderDetailsDTO
import com.wevx.dealershipmanagement.databinding.ItemLayoutProductCartBinding
import com.wevx.dealershipmanagement.domain.models.CartItem
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
            holder.binding.apply {
                tvName.text = "Banana"
                tvQuantity.text = "${product.quantity} pcs"
                tvSubtotal.text = "%.2f".format(product.subtotal)
            }
        }

    }

    override fun getItemCount(): Int = productsList.size

    class ViewHolder(val binding: ItemLayoutProductCartBinding) :
        RecyclerView.ViewHolder(binding.root)
}