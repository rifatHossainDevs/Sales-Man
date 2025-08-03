package com.wevx.dealershipmanagement.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wevx.dealershipmanagement.databinding.ItemDrawerItemsBinding
import com.wevx.dealershipmanagement.domain.models.DrawerItems

class DrawerItemAdapter(val drawerItems: List<DrawerItems>, private val onItemClick: (DrawerItems) -> Unit) :
    RecyclerView.Adapter<DrawerItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemDrawerItemsBinding.inflate(
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
        drawerItems[position].let { item->
            holder.binding.apply {
                tvName.text = item.name
                ivImage.setImageResource(item.image)

                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }

    }

    override fun getItemCount(): Int = drawerItems.size

    class ViewHolder(val binding: ItemDrawerItemsBinding) :
        RecyclerView.ViewHolder(binding.root)
}