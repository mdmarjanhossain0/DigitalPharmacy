package com.devscore.digital_pharmacy.purchase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.inventory.adapter.InventoryPageAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class PurchaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_purchase, container, false)

        val viewPager: ViewPager2 = view.findViewById(R.id.purchaseViewPager)
        val tabLayout: TabLayout = view.findViewById(R.id.purchaseTabLayout)

        viewPager.adapter = fragmentManager?.let { InventoryPageAdapter(it, lifecycle) }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Generated"
                1 -> tab.text = "Saved"
                2 -> tab.text = "Completed"
            }
        }.attach()


        return view
    }


}