package com.devscore.digital_pharmacy.inventory.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.inventory.Return1Fragment

class Return1Adapter(val context: Context) :
    RecyclerView.Adapter<Return1Adapter.Return1ViewHolder>() {

    class Return1ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Return1ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_retrun, parent, false)
        return Return1ViewHolder(view)
    }

    override fun onBindViewHolder(holder: Return1ViewHolder, position: Int) {


        holder.itemView.setOnClickListener {
            //  context.startActivity(Intent(context, BuyCourseActivity::class.java))
        }
    }

    override fun getItemCount(): Int {
        return 10
    }
}