package com.devscore.digital_pharmacy.presentation.purchases.purchasesinventory

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.presentation.util.GenericViewHolder
import kotlinx.android.synthetic.main.item_sales_inventory.view.*

class PurchasesInventoryAdapter
constructor(
    private val interaction: Interaction? = null
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "PurchasesInvAdapter"

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
        mrp = 0f,
        purchases_price = 0f,
        discount = 0f,
        is_percent_discount = false,
        manufacture = "",
        kind = "",
        form = "",
        remaining_quantity = 0f,
        damage_quantity = 0f,
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
        mrp = 0f,
        purchases_price = 0f,
        discount = 0f,
        is_percent_discount = false,
        manufacture = "",
        kind = "",
        form = "",
        remaining_quantity = 0f,
        damage_quantity = 0f,
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
                return SalesInventoryDataViewHolder(itemView, interaction)
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
        Log.d(TAG, "PurchasesInventoryAdapter List Size " + differ.currentList.size)
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
        private val adapter: PurchasesInventoryAdapter
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

    fun submitList(medicineList: List<LocalMedicine>?, isLoading : Boolean = true, queryExhausted : Boolean = false){
        val newList = medicineList?.toMutableList()
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
            itemView.salesInventoryBrandName.setText(item.brand_name)
            itemView.salesInventoryCompanyName.setText(item.generic)
            itemView.salesInventoryMRP.setText("MRP ??? "+ item.mrp.toString())



            itemView.salesInventoryItemCard.setOnClickListener {
                interaction?.onItemCard(adapterPosition, item)
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