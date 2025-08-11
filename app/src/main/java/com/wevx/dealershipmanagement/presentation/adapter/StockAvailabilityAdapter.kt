package com.wevx.dealershipmanagement.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.databinding.ItemLayoutProductStockAvailabilityBinding
import com.wevx.dealershipmanagement.domain.models.ProductModel

class StockAvailabilityAdapter(val products: List<ProductModel>) :
    RecyclerView.Adapter<StockAvailabilityAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemLayoutProductStockAvailabilityBinding.inflate(
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

        products[position].let { product ->
            holder.binding.apply {
                tvProductName.text = product.productName
                tvBrandName.text = product.brandName
                tvPrice.text = product.price.toString()
                tvStockQuantity.text = "${product.stockQuantity} ${product.unit}"
                tvIsActive.text = product.isActive.toString()

                if (product.isActive.toString() == "true") {
                    tvIsActive.text = "Active"
                } else {
                    tvIsActive.text = "Inactive"
                }
                Glide.with(holder.itemView.context)
                    .load(product.imageUrl.replace("http://", "https://"))
                    .placeholder(R.drawable.ic_product_24)
                    .error(R.drawable.ic_product_24)
                    .into(ivProduct)
            }
        }

    }

    override fun getItemCount(): Int = products.size

    class ViewHolder(val binding: ItemLayoutProductStockAvailabilityBinding) :
        RecyclerView.ViewHolder(binding.root)
}