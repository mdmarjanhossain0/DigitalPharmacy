package com.devscore.digital_pharmacy.presentation.inventory.global

import com.devscore.digital_pharmacy.business.domain.util.StateMessage

sealed class GlobalEvents {

    object NewMedicineSearch : GlobalEvents()

    object NextPage: GlobalEvents()

    data class UpdateQuery(val query: String): GlobalEvents()


    object GetOrderAndFilter: GlobalEvents()

    data class Error(val stateMessage: StateMessage): GlobalEvents()

    object OnRemoveHeadFromQueue: GlobalEvents()
}