package com.devscore.digital_pharmacy.business.domain.models

class CreateSalesOder (

    var customer : Int?,
    var total_mount : String?,
    var total_after_discount : String?,
    var paid_amount : String,
    var discount : String,
    var is_discount_ercent : Boolean,
    var oder_items : List<CreateSalesOderMedicine>
)


data class CreateSalesOderMedicine (
    var unit : Int,
    var quantity : String,
    var local_medicine : Int

)