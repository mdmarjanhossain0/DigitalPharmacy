package com.devscore.digital_pharmacy.presentation.sales.payment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.SalesCart
import com.devscore.digital_pharmacy.business.domain.models.SalesOrder
import com.devscore.digital_pharmacy.presentation.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.item_sales_list.*
import kotlinx.android.synthetic.main.item_sales_list.view.*
import java.util.ArrayList

class SalesOrdersAdapter(val context: Context) :
    RecyclerView.Adapter<SalesOrdersAdapter.SalesOrdersViewHolder>() {


    var recyclerItemAdapter : SalesOrderItemAdapter? = null

    class SalesOrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesOrdersViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_sales_list, parent, false)
        return SalesOrdersViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalesOrdersViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            holder.itemView.salesOrderItemRvId.apply {
                layoutManager = LinearLayoutManager(context)
                val topSpacingDecorator = TopSpacingItemDecoration(0)
                removeItemDecoration(topSpacingDecorator)
                hasFixedSize()
                addItemDecoration(topSpacingDecorator)
                recyclerItemAdapter = SalesOrderItemAdapter()
                adapter = recyclerItemAdapter
            }
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    fun submitList(order: SalesOrder) {
        if (recyclerItemAdapter != null) {
            recyclerItemAdapter?.submitList(order.sales_oder_medicines)
        }
    }
}