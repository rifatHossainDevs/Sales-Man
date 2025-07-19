package com.wevx.dealershipmanagement.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wevx.dealershipmanagement.databinding.ItemLayoutCustomerBinding
import com.wevx.dealershipmanagement.models.Customers

class CustomerAdapter(val customers: List<Customers>) : RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemLayoutCustomerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
val customer = customers[position]
        holder.binding.apply {
            tvShopName.text = customer.shopName
            tvOwner.text = customer.customerName
            tvAddress.text = customer.customerAddress
            ivCustomer.load(customer.customerImg)

        }
    }

    override fun getItemCount(): Int = customers.size

    class ViewHolder(val binding: ItemLayoutCustomerBinding) : RecyclerView.ViewHolder(binding.root)
}