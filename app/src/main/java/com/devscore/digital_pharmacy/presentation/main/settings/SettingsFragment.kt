package com.devscore.digital_pharmacy.presentation.main.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.devscore.digital_pharmacy.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*


@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {


    private val viewModel : SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        logout_button.setOnClickListener {
            logout()
        }
    }



    fun logout(){
        viewModel.onTriggerEvent(SettingsEvents.Logout)
    }
}