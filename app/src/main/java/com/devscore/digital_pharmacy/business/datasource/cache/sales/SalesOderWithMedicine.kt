package com.devscore.digital_pharmacy.business.datasource.cache.sales

import androidx.room.Embedded
import androidx.room.Relation
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.LocalMedicineEntity
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.LocalMedicineUnitsEntity
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.MedicineUnits
import com.devscore.digital_pharmacy.business.domain.models.SalesOder

data class SalesOderWithMedicine (

    @Embedded
    var sales_oder : SalesOderEntity,

    @Relation(
        parentColumn = "pk",
        entityColumn = "sales_oder"
    )
    var sales_oder_medicines : List<SalesOderMedicineEntity>
)


fun SalesOderWithMedicine.toSalesOder() : SalesOder {
    return SalesOder(
        pk = sales_oder.pk,
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