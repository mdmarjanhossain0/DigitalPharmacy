package com.devscore.digital_pharmacy.inventory.adapter

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devscore.digital_pharmacy.inventory.AddProductSubMedicineFragment
import com.devscore.digital_pharmacy.inventory.AddProductSubNonMedicineFragment
import com.devscore.digital_pharmacy.presentation.inventory.global.GlobalFragment
import com.devscore.digital_pharmacy.presentation.inventory.local.LocalFragment

class InventoryAddProductPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment = Fragment()

        when (position) {
            0 -> fragment = AddProductSubMedicineFragment()
            1 -> fragment = AddProductSubNonMedicineFragment()
        }

        return fragment
    }

}