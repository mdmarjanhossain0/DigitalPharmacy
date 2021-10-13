package com.devscore.digital_pharmacy.presentation.main.settings

sealed class SettingsEvents {


    object Logout : SettingsEvents()


    object OnRemoveHeadFromQueue : SettingsEvents()
}