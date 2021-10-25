package com.devscore.digital_pharmacy.business.domain.models

class CreateSalesOrder (

    var customer : Int?,
    var total_amount : Long?,
    var total_after_discount : Long?,
    var paid_amount : Long,
    var discount : Long?,
    var is_discount_percent : Boolean,
    var sales_oder_medicines : List<CreateSalesOrderMedicine>
)


data class CreateSalesOrderMedicine (
    var unit : Int,
    var quantity : Long,
    var local_medicine : Int

)

fun CreateSalesOrderMedicine.toSalesOrderMedicine() : SalesOrderMedicine {
    return SalesOrderMedicine(
        unit = unit,
        quantity = quantity,
        local_medicine = local_medicine
    )
}


fun CreateSalesOrder.toSalesOder() : SalesOrder {
    return SalesOrder(
        customer = customer,
        total_amount = total_amount,
        total_after_discount = total_after_discount,
        paid_amount = paid_amount,
        discount = discount,
        is_discount_percent =is_discount_percent,
        sales_oder_medicines = sales_oder_medicines.map {
            it.toSalesOrderMedicine()
        }
    )
}