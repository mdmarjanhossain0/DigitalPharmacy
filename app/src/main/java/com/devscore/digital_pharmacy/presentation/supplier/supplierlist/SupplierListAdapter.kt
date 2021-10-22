package com.devscore.digital_pharmacy.presentation.supplier.supplierlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.Supplier
import com.devscore.digital_pharmacy.presentation.util.GenericViewHolder

class SupplierListAdapter
constructor(
    private val interaction: Interaction? = null
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "SupplierAdapter"

    companion object {

        const val IMAGE_ITEM = 1
        const val LOADING_ITEM = 2

        const val LOADING = 1
        const val RETRY =2
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            IMAGE_ITEM -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_local,parent,false)
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
        if(differ.currentList.size < (position + 1)){
            interaction?.nextPage()
            return LOADING_ITEM
        }
        Log.d(TAG, "Data Item")
        return IMAGE_ITEM
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
        return differ.currentList.size + 1
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Supplier>() {

        override fun areItemsTheSame(oldItem: Supplier, newItem: Supplier): Boolean {
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
//            Log.d("LocalAdapter", item.toString())
//            itemView.localBrandNameTV.setText(item.brand_name)
//            itemView.localCompanyNameTV.setText(item.generic)
//            if (item.mrp != null) {
//                itemView.localMRPTV.setText("MRP ৳ "+ item.mrp.toString())
//            }
//            else {
//                itemView.localMRPTV.setText("MRP ৳ ...")
//            }
//
//            itemView.localMedicineReturn.setOnClickListener {
//                interaction?.onItemReturnSelected(adapterPosition, item)
//            }
//
//            itemView.localMedicineDelete.setOnClickListener {
//                interaction?.onItemDeleteSelected(adapterPosition, item)
//            }

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