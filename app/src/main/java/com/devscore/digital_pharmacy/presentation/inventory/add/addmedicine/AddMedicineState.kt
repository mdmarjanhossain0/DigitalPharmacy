package com.devscore.digital_pharmacy.presentation.inventory.add.addmedicine

import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.MedicineUnits
import com.devscore.digital_pharmacy.business.domain.util.Queue
import com.devscore.digital_pharmacy.business.domain.util.StateMessage

data class AddMedicineState (
    val isLoading : Boolean = false,
    val medicine : LocalMedicine = LocalMedicine(
        id = -1,
        brand_name = null,
        sku = null,
        dar_number = null,
        mr_number = null,
        generic = null,
        indication = null,
        symptom = null,
        strength = null,
        description = null,
        mrp = null,
        purchases_price = null,
        discount = null,
        is_percent_discount = false,
        manufacture = null,
        kind = null,
        form = null,
        remaining_quantity = null,
        damage_quantity = null,
        rack_number = null,
        units = listOf<MedicineUnits>()
    ),
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)