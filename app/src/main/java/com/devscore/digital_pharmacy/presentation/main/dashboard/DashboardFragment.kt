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
import com.devscore.digital_pharmacy.presentation.customer.CustomerActivity
import com.devscore.digital_pharmacy.presentation.inventory.InventoryActivity
import com.devscore.digital_pharmacy.presentation.sales.SalesActivity
import com.devscore.digital_pharmacy.presentation.supplier.SupplierActivity
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View =  inflater.inflate(R.layout.fragment_dashboard, container, false)
        val inventoryImg: ImageView = view.findViewById(R.id.inventoryImgId)
        val salesImg: ImageView = view.findViewById(R.id.salesImgId)





        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUIClick()
    }

    private fun initUIClick() {

        inventoryImgId.setOnClickListener(){
            val intent = Intent(context, InventoryActivity::class.java)

            intent.putExtra("INVENTORY","inventory")

            startActivity(intent)
        }

        supplier_id.setOnClickListener {
            val intent = Intent(context, SupplierActivity::class.java)
            startActivity(intent)
        }

        customer_id.setOnClickListener {
            val intent = Intent(context, CustomerActivity::class.java)
            startActivity(intent)
        }


        salesImgId.setOnClickListener {
            val intent = Intent(context, SalesActivity::class.java)
            startActivity(intent)
        }
    }


}