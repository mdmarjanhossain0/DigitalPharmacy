package com.devscore.digital_pharmacy.business.domain.models

class CreatePurchasesOder (

    var vendor : Int?,
    var company : String?,
    var total_amount : Float?,
    var total_after_discount : Float?,
    var paid_amount : Float,
    var discount : Float?,
    var is_discount_percent : Boolean,
    var status : Int = 0,
    var purchases_order_medicines : List<CreatePurchasesOderMedicine>
)


data class CreatePurchasesOderMedicine (
    var unit : Int,
    var quantity : Float,
    var local_medicine : Int

)


fun CreatePurchasesOderMedicine.toPurchasesOrderMedicine() : PurchasesOrderMedicine {
    return PurchasesOrderMedicine(
        unit = unit,
        quantity = quantity,
        local_medicine = local_medicine
    )
}


fun CreatePurchasesOder.toPurchasesOrder() : PurchasesOrder {
    return PurchasesOrder(
        vendor = vendor,
        company = company,
        total_amount = total_amount,
        total_after_discount = total_after_discount,
        paid_amount = paid_amount,
        discount = discount,
        is_discount_percent =is_discount_percent,
        status = status,
        purchases_order_medicines = purchases_order_medicines.map {
            it.toPurchasesOrderMedicine()
        }
    )
}