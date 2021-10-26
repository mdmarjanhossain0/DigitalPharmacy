package com.devscore.digital_pharmacy.business.domain.models

import com.devscore.digital_pharmacy.business.datasource.cache.sales.*

class SalesOrder (

    var pk : Int? = -1,
    var room_id : Long? = -1,
    var customer : Int?,
    var total_amount : Float?,
    var total_after_discount : Float?,
    var paid_amount : Float?,
    var discount : Float?,
    var is_discount_percent : Boolean,
    var sales_oder_medicines : List<SalesOrderMedicine>?,
    var created_at : String? = null,
    var updated_at : String? = null
)



fun SalesOrder.toSalesOrderEntity() : SalesOrderEntity {
    return SalesOrderEntity(
        pk = pk,
        customer = customer,
        total_amount = total_amount,
        total_after_discount = total_after_discount,
        paid_amount = paid_amount,
        discount = discount,
        is_discount_percent =is_discount_percent,
        created_at = created_at!!,
        updated_at = updated_at
    )
}

fun SalesOrder.toSalesOrderMedicinesEntity() : List<SalesOrderMedicineEntity> {
    var list = mutableListOf<SalesOrderMedicineEntity>()
    for (medicine in sales_oder_medicines!!) {
        list.add(
            SalesOrderMedicineEntity(
                sales_oder = pk!!,
                pk = pk,
                unit = medicine.unit,
                quantity = medicine.quantity,
                local_medicine = medicine.local_medicine,
                brand_name = medicine.brand_name
            )
        )
    }
    return list
}


fun SalesOrder.toFailureSalesOrderEntity() : FailureSalesOrderEntity {
    return FailureSalesOrderEntity(
        customer = customer,
        total_amount = total_amount,
        total_after_discount = total_after_discount,
        paid_amount = paid_amount,
        discount = discount,
        is_discount_percent =is_discount_percent,
        created_at = created_at!!,
        updated_at = updated_at
    )
}



fun SalesOrder.toFailureSalesOderMedicineEntity() : List<FailureSalesOrderMedicineEntity> {
    var list = mutableListOf<FailureSalesOrderMedicineEntity>()
    for (medicine in sales_oder_medicines!!) {
        list.add(
            FailureSalesOrderMedicineEntity(
                sales_oder = pk!!,
                unit = medicine.unit,
                quantity = medicine.quantity,
                local_medicine = medicine.local_medicine,
                brand_name = medicine.brand_name
            )
        )
    }
    return list
}