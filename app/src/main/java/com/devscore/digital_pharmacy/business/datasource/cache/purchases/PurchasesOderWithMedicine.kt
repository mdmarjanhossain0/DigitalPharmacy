package com.devscore.digital_pharmacy.business.datasource.cache.purchases

import androidx.room.Embedded
import androidx.room.Relation
import com.devscore.digital_pharmacy.business.domain.models.PurchasesOder

data class PurchasesOderWithMedicine (

    @Embedded
    var purchases_oder : PurchasesOderEntity,

    @Relation(
        parentColumn = "pk",
        entityColumn = "purchases_oder"
    )
    var purchases_oder_medicines : List<PurchasesOderMedicineEntity>
)


fun PurchasesOderWithMedicine.toPurchasesOder() : PurchasesOder {
    return PurchasesOder (
        pk = purchases_oder.pk,
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