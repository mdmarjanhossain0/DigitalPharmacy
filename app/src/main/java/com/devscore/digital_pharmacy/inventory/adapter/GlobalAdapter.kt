package com.devscore.digital_pharmacy.inventory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.inventory.InventoryAddProductFragment
import com.devscore.digital_pharmacy.inventory.Return1Fragment

class GlobalAdapter(val context: Context) :
    RecyclerView.Adapter<GlobalAdapter.GlobalViewHolder>() {

    class GlobalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cartImgId : ImageView = itemView.findViewById(R.id.cartImgId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobalViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_global, parent, false)
        return GlobalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GlobalViewHolder, position: Int) {

        holder.cartImgId.setOnClickListener(){

            (context as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerId, InventoryAddProductFragment()).commit()
        }

        holder.itemView.setOnClickListener {
            //  context.startActivity(Intent(context, BuyCourseActivity::class.java))
        }
    }

    override fun getItemCount(): Int {
        return 10
    }
}