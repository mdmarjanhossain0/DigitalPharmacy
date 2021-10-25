package com.devscore.digital_pharmacy.business.domain.models

data class PurchasesOderMedicine (

    var pk : Int? = -1,
    var room_id : Long? = -1,
    var unit : Int,
    var quantity : Long,
    var local_medicine : Int,
    var brand_name : String? = null

)