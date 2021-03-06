package com.devscore.digital_pharmacy.presentation.purchases.payment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.PurchasesCart
import com.devscore.digital_pharmacy.business.domain.models.PurchasesOrder
import com.devscore.digital_pharmacy.presentation.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.item_sales_list.view.*

class PurchasesOrdersAdapter
constructor(
    private val interaction: PurchasesOrderItemAdapter.Interaction? = null
)
    :
    RecyclerView.Adapter<PurchasesOrdersAdapter.PurchasesOrdersViewHolder>() {


    var recyclerItemAdapter : PurchasesOrderItemAdapter? = null

    class PurchasesOrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchasesOrdersViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_sales_list, parent, false)
        return PurchasesOrdersViewHolder(view)
    }

    override fun onBindViewHolder(holder: PurchasesOrdersViewHolder, position: Int) {
//        holder.itemView.setOnClickListener {
//        }
        Log.d("AppDebug", "onBind")
        holder.itemView.salesOrderItemRvId.apply {
            layoutManager = LinearLayoutManager(context)
            val topSpacingDecorator = TopSpacingItemDecoration(0)
            removeItemDecoration(topSpacingDecorator)
            hasFixedSize()
            addItemDecoration(topSpacingDecorator)
            if (recyclerItemAdapter == null) {
                recyclerItemAdapter = PurchasesOrderItemAdapter(interaction)
            }
            adapter = recyclerItemAdapter
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    fun submitList(order: PurchasesOrder, cartList : List<PurchasesCart>) {
        if (recyclerItemAdapter == null) {
            recyclerItemAdapter = PurchasesOrderItemAdapter(interaction)
        }
        recyclerItemAdapter?.submitList(cartList)
        notifyDataSetChanged()
    }
}