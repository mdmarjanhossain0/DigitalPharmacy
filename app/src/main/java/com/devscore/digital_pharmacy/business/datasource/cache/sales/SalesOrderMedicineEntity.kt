package com.devscore.digital_pharmacy.business.datasource.cache.sales

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.domain.models.SalesOrderMedicine


@Entity(
    tableName = "SalesOrderMedicine",
    foreignKeys = [
        ForeignKey(
            entity = SalesOrderEntity::class,
            parentColumns = ["pk"],
            childColumns = ["sales_oder"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SalesOrderMedicineEntity (


    var sales_oder : Int,

    @PrimaryKey(autoGenerate = false)
    var pk : Int?,

    var unit : Int,

    var quantity : Long,

    var local_medicine : Int,

    var brand_name : String?

)

fun SalesOrderMedicineEntity.toSaleOrderMedicine() : SalesOrderMedicine {
    return SalesOrderMedicine(
        pk = pk,
        unit = unit,
        quantity = quantity,
        local_medicine = local_medicine,
        brand_name = brand_name
    )
}