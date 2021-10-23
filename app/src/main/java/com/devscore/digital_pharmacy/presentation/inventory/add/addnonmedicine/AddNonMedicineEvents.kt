package com.devscore.digital_pharmacy.presentation.inventory.add.addnonmedicine

import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.util.StateMessage

sealed class AddNonMedicineEvents {

    object NewAddNonMedicine : AddNonMedicineEvents()

    data class CacheState(val local_medicine : LocalMedicine): AddNonMedicineEvents()

    data class Error(val stateMessage: StateMessage): AddNonMedicineEvents()

    object OnRemoveHeadFromQueue: AddNonMedicineEvents()
}