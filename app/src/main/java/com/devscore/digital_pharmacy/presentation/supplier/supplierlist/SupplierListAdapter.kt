package com.devscore.digital_pharmacy.presentation.supplier.supplierlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.Supplier
import com.devscore.digital_pharmacy.presentation.purchases.orderlist.PurchasesOrderAdapter
import com.devscore.digital_pharmacy.presentation.util.GenericViewHolder
import kotlinx.android.synthetic.main.item_supplier_list.view.*

class SupplierListAdapter
constructor(
    private val interaction: Interaction? = null
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "SupplierAdapter"



    val loading = Supplier(
        pk = -2,
        company_name = "",
        agent_name = "",
        email = "",
        mobile = "",
        whatsapp = "",
        facebook = "",
        imo = "",
        address = ""
    )

    val notFound = Supplier(
        pk = -3,
        company_name = "",
        agent_name = "",
        email = "",
        mobile = "",
        whatsapp = "",
        facebook = "",
        imo = "",
        address = ""
    )

    companion object {

        const val IMAGE_ITEM = 1
        const val LOADING_ITEM = 2

        const val LOADING = 1
        const val RETRY =2
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            IMAGE_ITEM -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_supplier_list,parent,false)
                return SupplierDataViewHolder(itemView, interaction)
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
        return SupplierDataViewHolder(itemView, interaction)
    }

    override fun getItemViewType(position: Int): Int {
        if (differ.currentList.size != 0) {
            if(differ.currentList.get(position).pk == -2){
                return PurchasesOrderAdapter.LOADING_ITEM
            }
            if(differ.currentList.get(position).pk == -3){
                return PurchasesOrderAdapter.NOT_FOUND
            }
            return PurchasesOrderAdapter.IMAGE_ITEM
        }
        else {
            return PurchasesOrderAdapter.LOADING_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SupplierDataViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }

    }

    override fun getItemCount(): Int {
        Log.d(TAG, "GlobalAdapter List Size " + differ.currentList.size)
        return differ.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Supplier>() {

        override fun areItemsTheSame(oldItem: Supplier, newItem: Supplier): Boolean {
            if (oldItem.pk == null || newItem.pk == null) {
                return oldItem.room_id == oldItem.room_id
            }
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: Supplier, newItem: Supplier): Boolean {
            return oldItem == newItem
        }
    }

    private val differ =
        AsyncListDiffer(
            LocalRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

    internal inner class LocalRecyclerChangeCallback(
        private val adapter: SupplierListAdapter
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

    fun submitList(list: List<Supplier>?, ){
        val newList = list?.toMutableList()
        differ.submitList(newList)
    }

    fun changeBottom(bottomState : Int) {
    }

    class SupplierDataViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Supplier) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.supplierCompanyName.setText(item.company_name)
            itemView.supplierName.setText(item.agent_name)
            itemView.supplierContactNumber.setText(item.mobile)
            itemView.supplierTotalBalance.setText(item.total_balance.toString())

        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: Supplier)

        fun onItemReturnSelected(position: Int, item: Supplier)

        fun onItemDeleteSelected(position: Int, item: Supplier)

        fun restoreListPosition()

        fun nextPage()
    }
}