package com.devscore.digital_pharmacy.business.datasource.cache.sales

import androidx.room.Entity
import androidx.room.ForeignKey
import com.devscore.digital_pharmacy.business.domain.models.SalesOderMedicine

@Entity(
    tableName = "FailureSalesOderMedicine",
    foreignKeys = [
        ForeignKey(
            entity = FailureSalesOderMedicineEntity::class,
            parentColumns = ["room_id"],
            childColumns = ["sales_oder"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FailureSalesOderMedicineEntity (


    var sales_oder : Int,

    var unit : Int,

    var quantity : Long,

    var local_medicine : Int

)

fun FailureSalesOderMedicineEntity.toSaleOderMedicine() : SalesOderMedicine {
    return SalesOderMedicine(
        unit = unit,
        quantity = quantity,
        local_medicine = local_medicine
    )
}