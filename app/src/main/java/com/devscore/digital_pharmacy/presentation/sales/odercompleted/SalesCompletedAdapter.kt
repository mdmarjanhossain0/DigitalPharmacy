package com.devscore.digital_pharmacy.presentation.sales.odercompleted

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.SalesOrder
import com.devscore.digital_pharmacy.presentation.util.GenericViewHolder
import kotlinx.android.synthetic.main.item_sales_completed.view.*

class SalesCompletedAdapter
constructor(
    private val interaction: Interaction? = null
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "SalesCompletedAdapter"

    var isLoading : Boolean = true

    val loadingItem = SalesOrder (
        pk = -2,
        customer = -1,
        total_amount = 0f,
        total_after_discount = .05f,
        paid_amount = 0f,
        discount = 0f,
        is_discount_percent = false,
        status = 0,
        created_at = "",
        updated_at = "",
        sales_oder_medicines = null
    )



    val notFound = SalesOrder (
        pk = -3,
        customer = -1,
        total_amount = 0f,
        total_after_discount = 0f,
        paid_amount = 0f,
        discount = 0f,
        is_discount_percent = false,
        status = 0,
        created_at = "",
        updated_at = "",
        sales_oder_medicines = null
    )


    companion object {

        const val IMAGE_ITEM = 1
        const val LOADING_ITEM = 2
        const val NOT_FOUND = 3

        const val LOADING = 1
        const val RETRY =2
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            IMAGE_ITEM -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_sales_completed,parent,false)
                return SalesOrderDataViewHolder(itemView, interaction)
            }

            LOADING_ITEM -> {
                Log.d(TAG, "onCreateViewHolder: No more results...")
                return GenericViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_loading,
                        parent,
                        false
                    )
                )
            }
            else -> {
                return GenericViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_result_not_found,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (differ.currentList.size != 0) {
            if(differ.currentList.get(position).pk == -2){
                return LOADING_ITEM
            }
            if(differ.currentList.get(position).pk == -3){
                return NOT_FOUND
            }
            return IMAGE_ITEM
        }
        else {
            return LOADING_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SalesOrderDataViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }

    }

    override fun getItemCount(): Int {
        Log.d(TAG, "SalesOrderAdapter List Size " + differ.currentList.size)
        return differ.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SalesOrder>() {

        override fun areItemsTheSame(oldItem: SalesOrder, newItem: SalesOrder): Boolean {
            return oldItem.pk == newItem.pk
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: SalesOrder, newItem: SalesOrder): Boolean {
            return oldItem == newItem
        }
    }

    private val differ =
        AsyncListDiffer(
            GlobalRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

    internal inner class GlobalRecyclerChangeCallback(
        private val adapter: SalesCompletedAdapter
    ) : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }

    fun submitList(list: List<SalesOrder>?, isLoading : Boolean = true, queryExhausted : Boolean = false){
        val newList = list?.toMutableList()
        if (isLoading) {
            newList?.add(loadingItem)
        }
        else {
            if (queryExhausted) {
                newList?.add(notFound)
            }
        }
        differ.submitList(newList)
    }

    fun changeBottom(bottomState : Int) {
    }

    class SalesOrderDataViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: SalesOrder) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.orderCompletedOrderId.setText("Order ID : # " + item.pk.toString())
            var orderMedicines : String? = ""
            for (medicine in item.sales_oder_medicines!!) {
                orderMedicines = orderMedicines + medicine.brand_name
            }
            itemView.orderCompletedOrderMedicines.setText(orderMedicines)
            orderCompletedTotal.setText("MRP: ৳ " + item.paid_amount!!)


        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: SalesOrder)

        fun restoreListPosition()

        fun nextPage()
    }
}