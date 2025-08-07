package com.wevx.dealershipmanagement.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wevx.dealershipmanagement.databinding.ItemDrawerItemsBinding
import com.wevx.dealershipmanagement.databinding.ItemLayoutPendingAndCompleteOrderBinding
import com.wevx.dealershipmanagement.domain.models.DrawerItems
import com.wevx.dealershipmanagement.domain.models.PendingOrderModel
import androidx.core.graphics.toColorInt
import com.wevx.dealershipmanagement.utils.DateFormatter

class PendingOrderAdapter(val pendingOrders: List<PendingOrderModel>, val type: String) :
    RecyclerView.Adapter<PendingOrderAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemLayoutPendingAndCompleteOrderBinding.inflate(
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

        pendingOrders[position].let { item ->
            val dateFormatter = DateFormatter()
            val shipmentDate = dateFormatter.formatDateToDDMMYY(item.shipmentDate)
            holder.binding.apply {
                tvInvoiceNumber.text = "Invoice: ${item.invoiceNumber}"
                tvShipmentDate.text = "Shipment: ${shipmentDate}"
                if (item.paymentStatus == "Pending") {
                    tvPaymentStatus.text = "Status: ${item.paymentStatus} - ${item.due}"
                    tvPaymentStatus.setTextColor(Color.RED)
                } else {
                    tvPaymentStatus.text = "Status: ${item.paymentStatus}"
                    tvPaymentStatus.setTextColor(Color.GREEN)
                }
                tvShipmentAddress.text = item.shipmentAddress
                tvTotal.text = "Total: ${item.totalPrice}"

                if (type == "pending"){
                    holder.itemView.setBackgroundColor("#FFEBEE".toColorInt())
                }else{
                    holder.itemView.setBackgroundColor("#E8F5E9".toColorInt())
                }
            }
        }

    }

    override fun getItemCount(): Int = pendingOrders.size

    class ViewHolder(val binding: ItemLayoutPendingAndCompleteOrderBinding) :
        RecyclerView.ViewHolder(binding.root)
}