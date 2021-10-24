package com.devscore.digital_pharmacy.business.domain.models

import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.LocalMedicineEntity
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.LocalMedicineUnitsEntity
import com.devscore.digital_pharmacy.business.datasource.cache.sales.*

class SalesOder (

    var pk : Int? = -1,
    var room_id : Long? = -1,
    var customer : Int?,
    var total_amount : Long?,
    var total_after_discount : Long?,
    var paid_amount : Long?,
    var discount : Long?,
    var is_discount_percent : Boolean,
    var sales_oder_medicines : List<SalesOderMedicine>,
    var created_at : String? = null,
    var updated_at : String? = null
)



fun SalesOder.toSalesOderEntity() : SalesOderEntity {
    return SalesOderEntity(
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

fun SalesOder.toSalesOderMedicinesEnity() : List<SalesOderMedicineEntity> {
    var list = mutableListOf<SalesOderMedicineEntity>()
    for (medicine in sales_oder_medicines!!) {
        list.add(
            SalesOderMedicineEntity(
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


fun SalesOder.toFailureSalesOderEntity() : FailureSalesOderEntity {
    return FailureSalesOderEntity(
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



fun SalesOder.toFailureSalesOderMedicineEntity() : List<FailureSalesOderMedicineEntity> {
    var list = mutableListOf<FailureSalesOderMedicineEntity>()
    for (medicine in sales_oder_medicines!!) {
        list.add(
            FailureSalesOderMedicineEntity(
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