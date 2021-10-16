package com.devscore.digital_pharmacy.presentation.inventory.local

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.presentation.inventory.InventoryActivity

class LocalFragment : Fragment(), LocalAdapter.Interaction {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val view: View = inflater.inflate(R.layout.fragment_local, container, false)
        val localRv: RecyclerView? = view.findViewById(R.id.localRvId)


        if (localRv != null) {
            localRv.layoutManager = LinearLayoutManager(context)
            localRv.adapter = context?.let { LocalAdapter(this@LocalFragment, it) }
        }

        return view
    }

    override fun onItemSelected(position: Int) {
        (activity as InventoryActivity).navigateLocalFragmentToReturnFragment()
    }
}