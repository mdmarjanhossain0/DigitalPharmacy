package com.devscore.digital_pharmacy.business.datasource.cache.sales

import androidx.room.Embedded
import androidx.room.Relation
import com.devscore.digital_pharmacy.business.domain.models.SalesOder

data class FailureSalesOderWithMedicine (

    @Embedded
    var sales_oder : FailureSalesOderEntity,

    @Relation(
        parentColumn = "pk",
        entityColumn = "sales_oder"
    )
    var sales_oder_medicines : List<FailureSalesOderMedicineEntity>
)


fun FailureSalesOderWithMedicine.toSalesOder() : SalesOder {
    return SalesOder(
        room_id = sales_oder.room_id,
        customer = sales_oder.customer,
        total_amount = sales_oder.total_amount,
        total_after_discount = sales_oder.total_after_discount,
        paid_amount = sales_oder.paid_amount,
        discount = sales_oder.discount,
        is_discount_percent = sales_oder.is_discount_percent,
        created_at = sales_oder.created_at,
        updated_at = sales_oder.updated_at,
        sales_oder_medicines = sales_oder_medicines.map {
            it.toSaleOderMedicine()
        }
    )
}