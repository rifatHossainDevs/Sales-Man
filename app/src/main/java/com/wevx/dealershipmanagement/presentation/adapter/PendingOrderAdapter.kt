package com.wevx.dealershipmanagement.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wevx.dealershipmanagement.domain.models.PendingAndCompleteOrderModel
import com.wevx.dealershipmanagement.databinding.ItemLayoutPendingOrderBinding
import com.wevx.dealershipmanagement.utils.DateFormatter

class PendingOrderAdapter(val pendingOrders: List<PendingAndCompleteOrderModel>, val listener: PendingHandleClickListener) :
    RecyclerView.Adapter<PendingOrderAdapter.ViewHolder>() {

    interface PendingHandleClickListener{
        fun selectPendingOrder(pendingOderId: String)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemLayoutPendingOrderBinding.inflate(
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
                tvShipmentDate.text = "Shipment: $shipmentDate"
                /*if (item.paymentStatus == "Pending") {
                    tvPaymentStatus.text = "Status: ${item.paymentStatus} - ${item.due}"
                    tvPaymentStatus.setTextColor(Color.RED)
                } else {
                    tvPaymentStatus.text = "Status: ${item.paymentStatus}"
                    tvPaymentStatus.setTextColor(Color.GREEN)
                }*/
                tvShipmentAddress.text = item.shipmentAddress
                tvTotal.text = "৳${item.totalPrice}"


                root.setOnClickListener {
                    listener.selectPendingOrder(item.id)
                }
            }

        }


    }

    override fun getItemCount(): Int = pendingOrders.size

    class ViewHolder(val binding: ItemLayoutPendingOrderBinding) :
        RecyclerView.ViewHolder(binding.root)
}