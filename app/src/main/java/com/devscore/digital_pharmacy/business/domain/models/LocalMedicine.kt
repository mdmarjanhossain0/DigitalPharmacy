package com.devscore.digital_pharmacy.business.domain.models


data class LocalMedicine(
    var id: Int,

    var brand_name: String,

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

    var units: List<MedicineUnits>?
)


data class MedicineUnits(
    var id: Int,
    var quantity : Int,
    var name : String,
    var type : String
)