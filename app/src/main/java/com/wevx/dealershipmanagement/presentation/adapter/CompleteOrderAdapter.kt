package com.wevx.dealershipmanagement.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wevx.dealershipmanagement.domain.models.PendingAndCompleteOrderModel
import androidx.core.graphics.toColorInt
import com.wevx.dealershipmanagement.databinding.ItemLayoutCompleteOrderBinding
import com.wevx.dealershipmanagement.databinding.ItemLayoutPendingOrderBinding
import com.wevx.dealershipmanagement.utils.DateFormatter

class CompleteOrderAdapter(val completeOrders: List<PendingAndCompleteOrderModel>, val listener: CompleteHandleClickListener) :
    RecyclerView.Adapter<CompleteOrderAdapter.ViewHolder>() {

    interface CompleteHandleClickListener{
        fun selectCompleteOrder(completeOrderid: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemLayoutCompleteOrderBinding.inflate(
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

        completeOrders[position].let { item ->
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
                    listener.selectCompleteOrder(item.id)
                }

            }
        }

    }

    override fun getItemCount(): Int = completeOrders.size

    class ViewHolder(val binding: ItemLayoutCompleteOrderBinding) :
        RecyclerView.ViewHolder(binding.root)
}