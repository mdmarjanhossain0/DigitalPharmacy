package com.devscore.digital_pharmacy.presentation.customer.customerlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.Customer
import com.devscore.digital_pharmacy.business.domain.models.Supplier
import com.devscore.digital_pharmacy.presentation.util.GenericViewHolder
import kotlinx.android.synthetic.main.item_customer_list.view.*
import kotlinx.android.synthetic.main.item_supplier_list.view.*

class CustomerListAdapter
constructor(
    private val interaction: Interaction? = null
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "CustomerListAdapter"

    companion object {

        const val IMAGE_ITEM = 1
        const val LOADING_ITEM = 2

        const val LOADING = 1
        const val RETRY =2
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            IMAGE_ITEM -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_customer_list,parent,false)
                return CustomerDataViewHolder(itemView, interaction)
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
        }
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_local ,parent,false)
        return CustomerDataViewHolder(itemView, interaction)
    }

    override fun getItemViewType(position: Int): Int {
        if(differ.currentList.size < (position + 1)){
            interaction?.nextPage()
            return LOADING_ITEM
        }
        Log.d(TAG, "Data Item")
        return IMAGE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is CustomerDataViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }

    }

    override fun getItemCount(): Int {
        Log.d(TAG, "GlobalAdapter List Size " + differ.currentList.size)
        return differ.currentList.size + 1
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Customer>() {

        override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            if (oldItem.pk == null || newItem.pk == null) {
                return oldItem.room_id == oldItem.room_id
            }
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem == newItem
        }
    }

    private val differ =
        AsyncListDiffer(
            LocalRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

    internal inner class LocalRecyclerChangeCallback(
        private val adapter: CustomerListAdapter
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

    fun submitList(list: List<Customer>?, ){
        val newList = list?.toMutableList()
        differ.submitList(newList)
    }

    fun changeBottom(bottomState : Int) {
    }

    class CustomerDataViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Customer) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.customerName.setText(item.name)
            itemView.customerContactNumber.setText(item.mobile)

        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: Customer)

        fun onItemReturnSelected(position: Int, item: Customer)

        fun onItemDeleteSelected(position: Int, item: Customer)

        fun restoreListPosition()

        fun nextPage()
    }
}