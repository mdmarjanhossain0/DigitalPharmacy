package com.devscore.digital_pharmacy.business.datasource.cache.purchases

import androidx.room.Embedded
import androidx.room.Relation
import com.devscore.digital_pharmacy.business.domain.models.PurchasesOder


data class FailurePurchasesOderWithMedicine (

    @Embedded
    var purchases_oder : FailurePurchasesOderEntity,

    @Relation(
        parentColumn = "room_id",
        entityColumn = "sales_oder"
    )
    var purchases_oder_medicines : List<FailurePurchasesOderMedicineEntity>
)


fun FailurePurchasesOderWithMedicine.toPurchasesOder() : PurchasesOder {
    return PurchasesOder (
        room_id = purchases_oder.room_id,
        vendor = purchases_oder.vendor,
        company = purchases_oder.company,
        total_amount = purchases_oder.total_amount,
        total_after_discount = purchases_oder.total_after_discount,
        paid_amount = purchases_oder.paid_amount,
        discount = purchases_oder.discount,
        is_discount_percent = purchases_oder.is_discount_percent,
        created_at = purchases_oder.created_at,
        updated_at = purchases_oder.updated_at,
        purchases_oder_medicine = purchases_oder_medicines.map {
            it.toPurchasesOderMedicine()
        }
    )
}