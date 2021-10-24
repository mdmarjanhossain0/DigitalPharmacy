package com.devscore.digital_pharmacy.business.domain.models

class CreateSalesOder (

    var customer : Int?,
    var total_amount : Long?,
    var total_after_discount : Long?,
    var paid_amount : Long,
    var discount : Long?,
    var is_discount_percent : Boolean,
    var sales_oder_medicines : List<CreateSalesOderMedicine>
)


data class CreateSalesOderMedicine (
    var unit : Int,
    var quantity : Long,
    var local_medicine : Int

)

fun CreateSalesOderMedicine.toSalesOderMedicine() : SalesOderMedicine {
    return SalesOderMedicine(
        unit = unit,
        quantity = quantity,
        local_medicine = local_medicine
    )
}


fun CreateSalesOder.toSalesOder() : SalesOder {
    return SalesOder(
        customer = customer,
        total_amount = total_amount,
        total_after_discount = total_after_discount,
        paid_amount = paid_amount,
        discount = discount,
        is_discount_percent =is_discount_percent,
        sales_oder_medicines = sales_oder_medicines.map {
            it.toSalesOderMedicine()
        }
    )
}