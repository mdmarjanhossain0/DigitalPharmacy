package com.devscore.digital_pharmacy.presentation.inventory.add.addmedicine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.presentation.inventory.InventoryActivity
import kotlinx.android.synthetic.main.fragment_add_product_sub_medicine.*


class AddProductSubMedicineFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_product_sub_medicine, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addMedicineCancel.setOnClickListener {
            (activity as InventoryActivity).navigateAddMedicineFragmentToInventoryFragment()
        }
    }


}