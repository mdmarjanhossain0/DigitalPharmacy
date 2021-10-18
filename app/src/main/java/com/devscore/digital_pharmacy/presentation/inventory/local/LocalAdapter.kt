package com.devscore.digital_pharmacy.presentation.inventory.local

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.presentation.util.GenericViewHolder
import kotlinx.android.synthetic.main.item_global.view.*
import kotlinx.android.synthetic.main.item_local.view.*

class LocalAdapter
constructor(
    private val interaction: Interaction? = null
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "LocalAdapter"

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
                return LocalDataViewHolder(itemView, interaction)
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
        return LocalDataViewHolder(itemView, interaction)
    }

    override fun getItemViewType(position: Int): Int {
        if(differ.currentList.size < (position + 1)){
            interaction?.nextPage()
            return LOADING_ITEM
        }
        return IMAGE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is LocalDataViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }

    }

    override fun getItemCount(): Int {
        Log.d(TAG, "GlobalAdapter List Size " + differ.currentList.size)
        return differ.currentList.size + 1
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
        private val adapter: LocalAdapter
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

    class LocalDataViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: LocalMedicine) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            Log.d("LocalAdapter", item.toString())
            itemView.localBrandNameTV.setText(item.brand_name)
            itemView.localCompanyNameTV.setText(item.generic)
            itemView.localMRPTV.setText(item.mrp.toString())

            itemView.localMedicineReturn.setOnClickListener {
                interaction?.onItemReturnSelected(adapterPosition, item)
            }

            itemView.localMedicineDelete.setOnClickListener {
                interaction?.onItemDeleteSelected(adapterPosition, item)
            }

        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: LocalMedicine)

        fun onItemReturnSelected(position: Int, item: LocalMedicine)

        fun onItemDeleteSelected(position: Int, item: LocalMedicine)

        fun restoreListPosition()

        fun nextPage()
    }
}