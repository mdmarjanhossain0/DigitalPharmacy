package com.devscore.digital_pharmacy.business.domain.models

class PurchasesOder (

    var pk : Int? = -1,
    var room_id : Long? = -1,
    var vendor : Int?,
    var company : String?,
    var total_amount : Long?,
    var total_after_discount : Long?,
    var paid_amount : Long?,
    var discount : Long?,
    var is_discount_percent : Boolean,
    var purchases_oder_medicine : List<PurchasesOderMedicine>,
    var created_at : String? = null,
    var updated_at : String? = null
)