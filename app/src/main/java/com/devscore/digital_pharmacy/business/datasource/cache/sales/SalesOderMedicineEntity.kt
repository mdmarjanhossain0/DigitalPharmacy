package com.devscore.digital_pharmacy.business.datasource.cache.sales

import androidx.room.Entity
import androidx.room.ForeignKey
import com.devscore.digital_pharmacy.business.domain.models.SalesOderMedicine
import com.google.gson.annotations.SerializedName


@Entity(
    tableName = "SalesOderMedicine",
    foreignKeys = [
        ForeignKey(
            entity = SalesOderEntity::class,
            parentColumns = ["pk"],
            childColumns = ["sales_oder"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SalesOderMedicineEntity (


    var sales_oder : Int,

    var unit : Int,

    var quantity : Long,

    var local_medicine : Int

)

fun SalesOderMedicineEntity.toSaleOderMedicine() : SalesOderMedicine {
    return SalesOderMedicine(
        unit = unit,
        quantity = quantity,
        local_medicine = local_medicine
    )
}