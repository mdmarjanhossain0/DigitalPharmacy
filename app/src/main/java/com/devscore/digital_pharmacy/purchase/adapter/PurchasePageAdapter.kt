package com.devscore.digital_pharmacy.purchase.adapter

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devscore.digital_pharmacy.presentation.inventory.global.GlobalFragment
import com.devscore.digital_pharmacy.presentation.inventory.local.LocalFragment
import com.devscore.digital_pharmacy.purchase.PurchaseCompletedFragment
import com.devscore.digital_pharmacy.purchase.PurchaseGeneratedFragment
import com.devscore.digital_pharmacy.purchase.PurchaseSavedFragment

class PurchasePageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment = Fragment()

        when (position) {
            0 -> fragment = PurchaseGeneratedFragment()
            1 -> fragment = PurchaseSavedFragment()
            2 -> fragment = PurchaseCompletedFragment()
        }

        return fragment
    }

}