package com.devscore.digital_pharmacy.presentation.sales.card

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.devscore.digital_pharmacy.business.domain.models.SalesCart
import com.devscore.digital_pharmacy.business.domain.models.SalesOrderMedicine
import com.devscore.digital_pharmacy.presentation.util.GenericViewHolder
import kotlinx.android.synthetic.main.fragment_add_product_sub_medicine.*
import kotlinx.android.synthetic.main.item_sales_cart.view.*
import com.skydoves.powermenu.MenuAnimation

import androidx.core.content.ContextCompat
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.MedicineUnits

import com.skydoves.powermenu.CustomPowerMenu
import com.skydoves.powermenu.MenuBaseAdapter
import com.skydoves.powermenu.OnMenuItemClickListener
import kotlinx.android.synthetic.main.fragment_add_product_sub_medicine.view.*


class SalesCardAdapter
constructor(
    private val interaction: Interaction? = null
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "SalesCardAdapter"

    var isLoading : Boolean = true

    val loadingItem = SalesCart(
        medicine = null,
        orderMedicine = SalesOrderMedicine (
            pk = -2,
            room_id = -2,
            unit = -1,
            quantity = 0f,
            local_medicine = -1
        ),
        salesUnit = null,
        amount = null
    )



    val notFound = SalesCart(
        medicine = null,
        orderMedicine = SalesOrderMedicine (
            pk = -3,
            room_id = -3,
            unit = -1,
            quantity = 0f,
            local_medicine = -1
        ),
        salesUnit = null,
        amount = null
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
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_sales_cart,parent,false)
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
            if(differ.currentList.get(position).orderMedicine.pk == -2){
                return LOADING_ITEM
            }
            if(differ.currentList.get(position).orderMedicine.pk == -3){
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

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SalesCart>() {

        override fun areItemsTheSame(oldItem: SalesCart, newItem: SalesCart): Boolean {
            return oldItem.orderMedicine.pk == newItem.orderMedicine.pk
        }

        override fun areContentsTheSame(oldItem: SalesCart, newItem: SalesCart): Boolean {
            return oldItem == newItem
        }
    }

    private val differ =
        AsyncListDiffer(
            GlobalRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

    internal inner class GlobalRecyclerChangeCallback(
        private val adapter: SalesCardAdapter
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

    fun submitList(list: List<SalesCart>?, isLoading : Boolean = true, queryExhausted : Boolean = false){
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

        fun bind(item: SalesCart) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            if (item.salesUnit != null) {
                itemView.salesCardItemUnit.setText(item.salesUnit?.name)
            }

            val newList = mutableListOf<MedicineUnits>()
            for (a in item.medicine?.units!!) {
                newList.add(a)
            }

            val itemSelectListener = object : OnMenuItemClickListener<MedicineUnits?> {
                override fun onItemClick(position: Int, item: MedicineUnits?) {
                    itemView.salesCardItemUnit.setText(item?.name)
                    Log.d("AppDebug", item.toString())
                }
            }

            val customPowerMenu: CustomPowerMenu<*, *> =
                CustomPowerMenu.Builder(itemView.context, UnitMenuAdapter())
                    .addItemList(newList)
                    .setOnMenuItemClickListener(itemSelectListener)
                    .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
                    .setMenuRadius(10f)
                    .setMenuShadow(10f)
                    .build()




            itemView.salesCardItemBrandName.setText(item.orderMedicine.brand_name)

            itemView.salesCardItemUnit.setOnClickListener {
                customPowerMenu.showAsAnchorCenter(itemView.salesCardItemUnit)

            }
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: SalesCart)

        fun restoreListPosition()

        fun nextPage()
    }
}