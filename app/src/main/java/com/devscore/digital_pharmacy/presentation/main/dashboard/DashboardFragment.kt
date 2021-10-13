package com.devscore.digital_pharmacy.presentation.main.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.inventory.ContainerActivity


class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View =  inflater.inflate(R.layout.fragment_dashboard, container, false)
        val inventoryImg: ImageView = view.findViewById(R.id.inventoryImgId)
        val salesImg: ImageView = view.findViewById(R.id.salesImgId)

        inventoryImg.setOnClickListener(){
            val intent = Intent(context, ContainerActivity::class.java)

            intent.putExtra("INVENTORY","inventory")

            startActivity(intent)
        }

        salesImg.setOnClickListener(){
            val intent = Intent(context, ContainerActivity::class.java)
            intent.putExtra("SALES","sales")

            startActivity(intent)
        }



        return view
    }


}