package com.devscore.digital_pharmacy.presentation.inventory.local

import com.devscore.digital_pharmacy.business.domain.util.StateMessage

sealed class LocalMedicineEvents {

    object NewLocalMedicineSearch : LocalMedicineEvents()

    object NextPage: LocalMedicineEvents()

    data class UpdateQuery(val query: String): LocalMedicineEvents()


    object GetOrderAndFilter: LocalMedicineEvents()

    data class Error(val stateMessage: StateMessage): LocalMedicineEvents()

    object OnRemoveHeadFromQueue: LocalMedicineEvents()
}