package com.wevx.dealershipmanagement.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.wevx.dealershipmanagement.R

class CustomSpinnerAdapter(
    private val context: Context,
    private val items: List<String>,
    private val grayItems: Set<String> = emptySet()  // Items to show in gray
) : ArrayAdapter<String>(context, R.layout.spinner_item, items) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        val text = view.findViewById<TextView>(R.id.spinner_text)

        text.text = items[position]
        text.setTextColor(
            if (grayItems.contains(items[position]))
                ContextCompat.getColor(context, R.color.gray)
            else
                ContextCompat.getColor(context, R.color.black)
        )
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        val text = view.findViewById<TextView>(R.id.spinner_text)
        val icon = view.findViewById<ImageView>(R.id.spinner_icon)
        icon.visibility = View.GONE  // Hide icon in dropdown list

        text.text = items[position]
        text.setTextColor(
            if (grayItems.contains(items[position]))
                ContextCompat.getColor(context, R.color.gray)
            else
                ContextCompat.getColor(context, R.color.black)
        )
        return view
    }
}