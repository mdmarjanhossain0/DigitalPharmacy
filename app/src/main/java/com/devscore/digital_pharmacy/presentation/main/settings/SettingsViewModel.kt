package com.devscore.digital_pharmacy.presentation.main.settings

import androidx.lifecycle.ViewModel
import com.devscore.digital_pharmacy.presentation.session.SessionEvents
import com.devscore.digital_pharmacy.presentation.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel
@Inject
    constructor(
        private val sessionManager: SessionManager
    )
    : ViewModel() {

        init {

        }

    fun onTriggerEvent(event : SettingsEvents){
        when(event){
            SettingsEvents.Logout -> {
                logout()
            }
        }
    }

    fun logout(){
        sessionManager.onTriggerEvent(SessionEvents.Logout)
    }
}