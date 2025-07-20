package com.wevx.dealershipmanagement.recyclerView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wevx.dealershipmanagement.databinding.ItemLayoutProductCartBinding
import com.wevx.dealershipmanagement.models.Products

class ProductCartAdapter(val product: List<Products>) :
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
        product[position].let { product ->
            holder.binding.apply {
                tvName.text = product.productName
                tvQuantity.text = "${product.productQty} ${product.productUnit}"
                tvSubtotal.text = "%.2f".format(product.subtotal)

            }
        }

    }

    override fun getItemCount(): Int = product.size

    class ViewHolder(val binding: ItemLayoutProductCartBinding) :
        RecyclerView.ViewHolder(binding.root)
}