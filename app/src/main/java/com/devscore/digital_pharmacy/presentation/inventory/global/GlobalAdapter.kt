package com.devscore.digital_pharmacy.presentation.inventory.global

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.codingwithmitch.openapi.presentation.util.GenericViewHolder
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.GlobalMedicine
import com.devscore.digital_pharmacy.presentation.inventory.add.InventoryAddProductFragment

class GlobalAdapter
constructor(
    private val interaction: Interaction? = null
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "GlobalAdapter"

    companion object {

        const val IMAGE_ITEM = 1
        const val LOADING_ITEM = 2

        const val LOADING = 1
        const val RETRY =2
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            IMAGE_ITEM -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_global,parent,false)
                return ImageViewHolder(itemView, interaction)
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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_global,parent,false)
        return ImageViewHolder(itemView, interaction)
    }

    override fun getItemViewType(position: Int): Int {
        if(differ.currentList.size == (position + 1)){
            interaction?.nextPage()
            return LOADING_ITEM
        }
        return IMAGE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ImageViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GlobalMedicine>() {

        override fun areItemsTheSame(oldItem: GlobalMedicine, newItem: GlobalMedicine): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GlobalMedicine, newItem: GlobalMedicine): Boolean {
            return oldItem == newItem
        }
    }

    private val differ =
        AsyncListDiffer(
            ImageRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

    internal inner class ImageRecyclerChangeCallback(
        private val adapter: GlobalAdapter
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

    fun submitList(blogList: List<GlobalMedicine>?, ){
        val newList = blogList?.toMutableList()
        differ.submitList(newList)
    }

    fun changeBottom(bottomState : Int) {
    }

    class ImageViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: GlobalMedicine) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }


        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: GlobalMedicine)

        fun restoreListPosition()

        fun nextPage()
    }
}