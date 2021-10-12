package com.devscore.digital_pharmacy.presentation.auth.launcher

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.devscore.digital_pharmacy.R
import kotlinx.android.synthetic.main.fragment_launcher.*

class LauncherFragment : Fragment(R.layout.fragment_launcher) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("dsf", "dsfd")

        loginBtnId.setOnClickListener {
            navigateLoginFragment()
        }

        registerTvId.setOnClickListener {
            navigateRegisterFragment()
        }
    }


    private fun navigateLoginFragment() {
        findNavController().navigate(R.id.action_launcherFragment_to_loginFragment)
    }

    private fun navigateRegisterFragment() {
        findNavController().navigate(R.id.action_launcherFragment_to_registerFragment)
    }
}