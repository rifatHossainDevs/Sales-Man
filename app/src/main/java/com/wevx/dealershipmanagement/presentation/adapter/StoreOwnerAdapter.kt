package com.wevx.dealershipmanagement.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.databinding.ItemLayoutCustomerBinding
import com.wevx.dealershipmanagement.domain.models.StoreOwnerModel

class StoreOwnerAdapter(
    private var storeOwners: List<StoreOwnerModel>,
    private val listener: HandleCustomerClickListener
) : RecyclerView.Adapter<StoreOwnerAdapter.ViewHolder>() {

    interface HandleCustomerClickListener {
        fun selectCustomer(userId: String, id: String)
        fun editClickListener(storeOwnerModel: StoreOwnerModel)
        fun deleteClickListener(storeOwnerModel: StoreOwnerModel)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<StoreOwnerModel>) {
        storeOwners = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLayoutCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storeOwner = storeOwners[position]
        holder.binding.apply {
            tvShopName.text = storeOwner.storeName
            tvOwner.text = storeOwner.storeOwnerName
            tvAddress.text = storeOwner.address

            Glide.with(holder.itemView.context)
                .load(storeOwner.storeImg.replace("http://", "https://"))
                .placeholder(R.drawable.ic_store_24)
                .error(R.drawable.ic_store_24)
                .into(ivStore)

            root.setOnClickListener {
                listener.selectCustomer(storeOwner.userId, storeOwner.id)
            }

            ivEdit.setOnClickListener {
                listener.editClickListener(storeOwner)
            }

            ivDelete.setOnClickListener {
                listener.deleteClickListener(storeOwner)
            }
        }
    }

    override fun getItemCount(): Int = storeOwners.size

    class ViewHolder(val binding: ItemLayoutCustomerBinding) : RecyclerView.ViewHolder(binding.root)
}