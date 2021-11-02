package com.devscore.digital_pharmacy.presentation.inventory.add.addmedicine

import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.util.StateMessage

sealed class AddMedicineEvents {

    object NewAddMedicine : AddMedicineEvents()

    data class CacheState(val local_medicine : LocalMedicine): AddMedicineEvents()


    data class UpdateId(val id : Int) : AddMedicineEvents()

    object FetchData : AddMedicineEvents()

    data class Error(val stateMessage: StateMessage): AddMedicineEvents()

    object OnRemoveHeadFromQueue: AddMedicineEvents()
}