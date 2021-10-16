package com.devscore.digital_pharmacy.presentation.inventory.local

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.devscore.digital_pharmacy.presentation.inventory.inventoryreturn.InventoryReturnFragment
import androidx.fragment.app.FragmentActivity
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.inventory.InventorySalesHistoryFragment

class LocalAdapter(
    val interaction : Interaction?,
    val context: Context) :
    RecyclerView.Adapter<LocalAdapter.LocalViewHolder>() {

    class LocalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var returnImg :ImageView = itemView.findViewById(R.id.returnImgId)
        var historyImg :ImageView = itemView.findViewById(R.id.historyImgId)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_local, parent, false)
        return LocalViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocalViewHolder, position: Int) {

        holder.returnImg.setOnClickListener(){

//            (context as FragmentActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainerId, InventoryReturnFragment()).commit()
        }

        holder.historyImg.setOnClickListener(){

//            (context as FragmentActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainerId, InventorySalesHistoryFragment()).commit()
        }

        holder.itemView.setOnClickListener {
            //  context.startActivity(Intent(context, BuyCourseActivity::class.java))
            interaction?.onItemSelected(position)
        }
    }

    override fun getItemCount(): Int {
        return 10
    }


    interface Interaction {

        fun onItemSelected(position: Int)

    }
}