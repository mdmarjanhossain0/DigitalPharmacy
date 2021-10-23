package com.devscore.digital_pharmacy.business.domain.models

import com.devscore.digital_pharmacy.business.datasource.network.inventory.network_responses.AddMedicineResponse
import com.devscore.digital_pharmacy.business.datasource.network.inventory.toMedicineUnits

data class AddMedicine (

    var brand_name: String?,

    var sku: String?,

    var dar_number: String?,

    var mr_number: String?,

    var generic: String?,

    var indication: String?,

    var symptom: String?,

    var strength: String?,

    var description: String?,

    var mrp: Int?,

    var purchases_price : Int?,

    var discount: Int?,

    var is_percent_discount: Boolean,

    var manufacture: String?,

    var kind: String?,

    var form: String?,

    var remaining_quantity: Int?,

    var damage_quantity: Int?,

    var rack_number: String?,

    var units: List<MedicineUnits>
)

fun AddMedicine.toLocalMedicine() : LocalMedicine {
    return LocalMedicine(
        id = -1,
        brand_name = brand_name,
        sku = sku,
        dar_number = dar_number,
        mr_number = mr_number,
        generic = generic,
        indication = indication,
        symptom = symptom,
        strength = strength,
        description = description,
        mrp = mrp,
        purchases_price = purchases_price,
        discount = discount,
        is_percent_discount = is_percent_discount,
        manufacture = manufacture,
        kind = kind,
        form = form,
        remaining_quantity = remaining_quantity,
        damage_quantity = damage_quantity,
        rack_number = rack_number,
        units = units
    )
}
