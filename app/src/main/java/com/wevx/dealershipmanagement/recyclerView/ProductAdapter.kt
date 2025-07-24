package com.wevx.dealershipmanagement.recyclerView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wevx.dealershipmanagement.databinding.ItemLayoutProductBinding
import com.wevx.dealershipmanagement.databinding.ItemLayoutProductCartBinding
import com.wevx.dealershipmanagement.models.Products

class ProductAdapter(val product: List<Products>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemLayoutProductBinding.inflate(
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
                tvProductName.text = product.productName
                tvBrandName.text = product.brand
                tvPrice.text = "Price: ${product.productPricePerUnit.toString()}"
                tvSubtotal.text = "Sub total: %.2f".format(product.subtotal)
                ivProduct.load(product.imageUrl)

            }
        }

    }

    override fun getItemCount(): Int = product.size

    class ViewHolder(val binding: ItemLayoutProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}