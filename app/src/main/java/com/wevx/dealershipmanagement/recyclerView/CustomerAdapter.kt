package com.wevx.dealershipmanagement.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wevx.dealershipmanagement.databinding.ItemLayoutCustomerBinding
import com.wevx.dealershipmanagement.models.Customers

class CustomerAdapter(val customers: List<Customers>, val listener: HandleCustomerClickListener) :
    RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {

    interface HandleCustomerClickListener {
        fun selectCustomer(customerId: String)
        fun editClickListener(customer: Customers)
        fun deleteClickListener(customers: Customers)
    }

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
        customers[position].let { customers ->
            holder.binding.apply {
                tvShopName.text = customers.shopName
                tvOwner.text = customers.customerName
                tvAddress.text = customers.customerAddress
                ivCustomer.load(
                    customers.customerImg
                )

                root.setOnClickListener {
                    listener.selectCustomer(customers.customerId)
                }

                ivEdit.setOnClickListener {
                    listener.editClickListener(customers)
                }

                ivDelete.setOnClickListener {
                    listener.deleteClickListener(customers)
                }

            }
        }


    }

    override fun getItemCount(): Int = customers.size

    class ViewHolder(val binding: ItemLayoutCustomerBinding) : RecyclerView.ViewHolder(binding.root)
}