package com.devscore.digital_pharmacy.presentation.sales.salesinventory

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.*
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.presentation.inventory.global.GlobalAdapter
import com.devscore.digital_pharmacy.presentation.util.GenericViewHolder
import kotlinx.android.synthetic.main.fragment_sales_inventory.view.*
import kotlinx.android.synthetic.main.item_local.view.*
import kotlinx.android.synthetic.main.item_sales_inventory.view.*

class SalesInventoryAdapter
constructor(
    private val interaction: Interaction? = null
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "SalesInventoryAdapter"

    val loadingItem = LocalMedicine (
        id = -2,
        brand_name = "",
        sku = "",
        dar_number = "",
        mr_number = "",
        generic = "",
        indication = "",
        symptom = "",
        strength = "",
        description = "",
        mrp = 0,
        purchases_price = 0,
        discount = 0,
        is_percent_discount = false,
        manufacture = "",
        kind = "",
        form = "",
        remaining_quantity = 0,
        damage_quantity = 0,
        rack_number = "",
        units = listOf()
            )

    val notFound = LocalMedicine (
        id = -3,
        brand_name = "",
        sku = "",
        dar_number = "",
        mr_number = "",
        generic = "",
        indication = "",
        symptom = "",
        strength = "",
        description = "",
        mrp = 0,
        purchases_price = 0,
        discount = 0,
        is_percent_discount = false,
        manufacture = "",
        kind = "",
        form = "",
        remaining_quantity = 0,
        damage_quantity = 0,
        rack_number = "",
        units = listOf()
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
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_sales_inventory,parent,false)
                return SalesInventoryAdapter.SalesInventoryDataViewHolder(itemView, interaction)
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
            if(differ.currentList.get(position).id == -2){
                return LOADING_ITEM
            }
            if(differ.currentList.get(position).id == -3){
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
            is SalesInventoryDataViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }

    }

    override fun getItemCount(): Int {
        Log.d(TAG, "GlobalAdapter List Size " + differ.currentList.size)
        return differ.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LocalMedicine>() {

        override fun areItemsTheSame(oldItem: LocalMedicine, newItem: LocalMedicine): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LocalMedicine, newItem: LocalMedicine): Boolean {
            return oldItem == newItem
        }
    }

    private val differ =
        AsyncListDiffer(
            LocalRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

    internal inner class LocalRecyclerChangeCallback(
        private val adapter: SalesInventoryAdapter
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

    fun submitList(medicineList: List<LocalMedicine>?, ){
        val newList = medicineList?.toMutableList()
        differ.submitList(newList)
    }

    fun changeBottom(bottomState : Int) {
    }

    class SalesInventoryDataViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: LocalMedicine) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            Log.d("SalesInventoryAdapter", item.toString())
            itemView.localBrandNameTV.setText(item.brand_name)
            itemView.localCompanyNameTV.setText(item.generic)
            if (item.mrp != null) {
                itemView.localMRPTV.setText("MRP ৳ "+ item.mrp.toString())
            }
            else {
                itemView.localMRPTV.setText("MRP ৳ ...")
            }


            itemView.salesInventoryItemCard.setOnClickListener {
                interaction?.onItemCard(adapterPosition, item)
            }

            itemView.localMedicineReturn.setOnClickListener {
                interaction?.onItemCard(adapterPosition, item)
            }

            itemView.localMedicineDelete.setOnClickListener {
                interaction?.onItemDeleteSelected(adapterPosition, item)
            }

        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: LocalMedicine)

        fun onItemCard(position: Int, item: LocalMedicine)

        fun onItemDeleteSelected(position: Int, item: LocalMedicine)

        fun restoreListPosition()

        fun nextPage()
    }
}