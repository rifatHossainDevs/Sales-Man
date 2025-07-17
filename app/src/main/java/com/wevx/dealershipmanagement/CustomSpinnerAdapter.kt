package com.wevx.dealershipmanagement

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomSpinnerAdapter(
    private val context: Context,
    private val items: List<String>
) : ArrayAdapter<String>(context, R.layout.spinner_item, items) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        val text = view.findViewById<TextView>(R.id.spinner_text)
        text.text = items[position]
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        val text = view.findViewById<TextView>(R.id.spinner_text)
        view.findViewById<ImageView>(R.id.spinner_icon).visibility = View.GONE // hide in dropdown list
        text.text = items[position]
        return view
    }
}
