package com.devscore.digital_pharmacy.presentation.purchases.cart

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.*
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.MedicineUnits
import com.devscore.digital_pharmacy.business.domain.models.PurchasesCart
import com.devscore.digital_pharmacy.business.domain.models.SalesCart
import com.devscore.digital_pharmacy.presentation.sales.card.UnitMenuAdapter
import com.devscore.digital_pharmacy.presentation.util.GenericViewHolder
import com.skydoves.powermenu.CustomPowerMenu
import com.skydoves.powermenu.MenuAnimation
import kotlinx.android.synthetic.main.item_sales_cart.view.*

class PurchasesCartAdapter
constructor(
    private val interaction: Interaction? = null
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "PurchasesCartAdapter"

    var isLoading : Boolean = true

    val loadingItem = PurchasesCart(
        medicine = null,
        salesUnit = null,
        quantity = -2,
        amount = null
    )



    val notFound = PurchasesCart(
        medicine = null,
        salesUnit = null,
        quantity = -3,
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
                return PurchasesDataViewHolder(itemView, interaction)
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
            if(differ.currentList.get(position).quantity == -2){
                return LOADING_ITEM
            }
            if(differ.currentList.get(position).quantity == -3){
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
            is PurchasesDataViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }

    }

    override fun getItemCount(): Int {
        Log.d(TAG, "SalesOrderAdapter List Size " + differ.currentList.size)
        return differ.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PurchasesCart>() {

        override fun areItemsTheSame(oldItem: PurchasesCart, newItem: PurchasesCart): Boolean {
            return oldItem.medicine?.id == newItem.medicine?.id
        }

        override fun areContentsTheSame(oldItem: PurchasesCart, newItem: PurchasesCart): Boolean {
            return oldItem == newItem
        }
    }

    private val differ =
        AsyncListDiffer(
            GlobalRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

    internal inner class GlobalRecyclerChangeCallback(
        private val adapter: PurchasesCartAdapter
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

    fun submitList(list: List<PurchasesCart>?, isLoading : Boolean = true, queryExhausted : Boolean = false){
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

    class PurchasesDataViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: PurchasesCart) = with(itemView) {

            itemView.salesCardItemBrandName.setText(item.medicine!!.brand_name!!)



            val newList = mutableListOf<MedicineUnits>()
            for (a in item.medicine?.units!!) {
                newList.add(a)
            }
            val menu =
                CustomPowerMenu.Builder(itemView.context, UnitMenuAdapter())
                    .addItemList(newList)
                    .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
                    .setMenuRadius(10f)
                    .setMenuShadow(10f)

            val customPowerMenu = menu.build()
            customPowerMenu.setOnMenuItemClickListener { position, selectItem ->
                itemView.salesCardItemUnit.setText(selectItem?.name)
                interaction?.onChangeUnit(
                    position = adapterPosition,
                    item = item,
                    unitId = selectItem.id!!,
                    quantity = itemView.salesCartItemQuantityCount.text.toString().toInt())
                Log.d("AppDebug", item.toString())
                customPowerMenu.dismiss()
            }






            itemView.setOnClickListener {
//                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.salesCardItemUnit.setOnClickListener {
                customPowerMenu.showAsAnchorCenter(itemView.salesCardItemUnit)

            }

            itemView.salesCardItemIncrease.setOnClickListener {
                val value = itemView.salesCartItemQuantityCount.text.toString().toInt() + 1
                itemView.salesCartItemQuantityCount.setText(value.toString())
            }
            itemView.salesCartItemDecrease.setOnClickListener {
                val value = itemView.salesCartItemQuantityCount.text.toString().toInt() - 1
                itemView.salesCartItemQuantityCount.setText(value.toString())
            }


            itemView.salesCartItemQuantityCount.doOnTextChanged { text, start, before, count ->
                try {
                    val value = itemView.salesCartItemQuantityCount.text.toString().toInt()
                    if (value < 1) {
                        itemView.salesCartItemQuantityCount.setText("1")
                        interaction?.alertDialog(item, "It cann't decrease")
                        return@doOnTextChanged
                    }
                    interaction?.onChangeUnit(
                        position = adapterPosition,
                        item = item,
                        unitId = item.salesUnit?.id!!,
                        quantity = value)
                }
                catch (e : Exception) {
                    itemView.salesCartItemQuantityCount.setText("1")
                    interaction?.alertDialog(item, "It cann't decrease")
                    return@doOnTextChanged
                }
            }

            itemView.salesCartSubTotal.setText("Sub Total ৳ " + item.amount)
            if (item.salesUnit != null) {
                itemView.salesCardItemUnit.setText(item.salesUnit?.name)
            }


            itemView.salesCartItemQuantityCount.setText(item.quantity.toString())
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: PurchasesCart)

        fun onChangeUnit(position: Int, item: PurchasesCart, unitId : Int, quantity : Int)

        fun alertDialog(item : PurchasesCart, message : String)

        fun restoreListPosition()

        fun nextPage()
    }
}