package com.devscore.digital_pharmacy.presentation.sales.payment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devscore.digital_pharmacy.R

class SalesOrdersAdapter(val context: Context) :
    RecyclerView.Adapter<SalesOrdersAdapter.SalesOrdersViewHolder>() {

    class SalesOrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesOrdersViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_sales_list, parent, false)
        return SalesOrdersViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalesOrdersViewHolder, position: Int) {
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return 1
    }
}