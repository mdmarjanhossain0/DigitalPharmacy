package com.devscore.digital_pharmacy.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.inventory.adapter.GlobalAdapter

class GlobalFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_global, container, false)
        val localRv: RecyclerView? = view.findViewById(R.id.globalRvId)

        if (localRv != null) {
            localRv.layoutManager = LinearLayoutManager(context)
            localRv.adapter = context?.let { GlobalAdapter(it) }
        }

        return view
    }


}