package com.devscore.digital_pharmacy.business.domain.models

class CreatePurchasesOder (

    var customer : Int?,
    var total_amount : Long?,
    var total_after_discount : Long?,
    var paid_amount : Long,
    var discount : Long?,
    var is_discount_percent : Boolean,
    var purchases_oder_medicine : List<CreatePurchasesOderMedicine>
)


data class CreatePurchasesOderMedicine (
    var unit : Int,
    var quantity : Long,
    var local_medicine : Int

)