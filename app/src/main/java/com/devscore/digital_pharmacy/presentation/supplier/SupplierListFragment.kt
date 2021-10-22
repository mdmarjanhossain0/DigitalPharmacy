package com.devscore.digital_pharmacy.presentation.supplier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.devscore.digital_pharmacy.R
import kotlinx.android.synthetic.main.fragment_supplier_list.*


class SupplierListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_supplier_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUIClick()
    }

    private fun initUIClick() {
        supplierFloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_supplierListFragment_to_createSupplierFragment)
        }
    }


}