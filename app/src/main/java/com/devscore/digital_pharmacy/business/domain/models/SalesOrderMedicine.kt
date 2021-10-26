package com.devscore.digital_pharmacy.business.domain.models

data class SalesOrderMedicine (

    var pk : Int? = -1,
    var room_id : Long? = -1,
    var unit : Int,
    var quantity : Float,
    var local_medicine : Int,
    var brand_name : String? = null

)