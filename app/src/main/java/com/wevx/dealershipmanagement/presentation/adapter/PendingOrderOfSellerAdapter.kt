package com.wevx.dealershipmanagement.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wevx.dealershipmanagement.databinding.ItemLayoutTodaysDeliveryBinding
import com.wevx.dealershipmanagement.domain.models.PendingOrderSellerModel

class PendingOrderOfSellerAdapter(
    val pendingOrders: List<PendingOrderSellerModel>,
    val listener: SellerPendingOrderHandleClickListener
) :
    RecyclerView.Adapter<PendingOrderOfSellerAdapter.ViewHolder>() {

    interface SellerPendingOrderHandleClickListener {
        fun selectPendingOrder(pendingOderId: String, customerId: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemLayoutTodaysDeliveryBinding.inflate(
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

        pendingOrders[position].let { order ->


            holder.binding.apply {
                tvInvoiceNumber.text = "Invoice: ${order.invoiceNumber}"
                tvPaymentStatus.text = "Status: ${order.paymentStatus}"
                tvShipmentAddress.text = order.address
                tvTotal.text = order.total


                root.setOnClickListener {
                    listener.selectPendingOrder(order.id, order.customerId)
                }
            }

        }


    }

    override fun getItemCount(): Int = pendingOrders.size

    class ViewHolder(val binding: ItemLayoutTodaysDeliveryBinding) :
        RecyclerView.ViewHolder(binding.root)
}