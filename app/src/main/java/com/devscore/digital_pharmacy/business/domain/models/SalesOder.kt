package com.devscore.digital_pharmacy.business.domain.models

import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.LocalMedicineEntity
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.LocalMedicineUnitsEntity
import com.devscore.digital_pharmacy.business.datasource.cache.sales.SalesOderEntity
import com.devscore.digital_pharmacy.business.datasource.cache.sales.SalesOderMedicineEntity
import com.devscore.digital_pharmacy.business.datasource.cache.sales.toSaleOderMedicine

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
    var created_at : String,
    var updated_at : String?
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
        created_at = created_at,
        updated_at = updated_at,
    )
}

fun SalesOder.toSalesOderMedicines() : List<SalesOderMedicineEntity> {
    var list = mutableListOf<SalesOderMedicineEntity>()
    for (medicine in sales_oder_medicines!!) {
        list.add(
            SalesOderMedicineEntity(
                sales_oder = pk!!,
                unit = medicine.unit,
                quantity = medicine.quantity,
                local_medicine = medicine.local_medicine
            )
        )
    }
    return list
}