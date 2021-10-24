package com.devscore.digital_pharmacy.business.datasource.cache.sales

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.domain.models.SalesOderMedicine

@Entity(
    tableName = "FailureSalesOderMedicine",
    foreignKeys = [
        ForeignKey(
            entity = FailureSalesOderEntity::class,
            parentColumns = ["room_id"],
            childColumns = ["sales_oder"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FailureSalesOderMedicineEntity (


    var sales_oder : Int,


    @PrimaryKey(autoGenerate = true)
    var room_id : Long? = -1,

    var unit : Int,

    var quantity : Long,

    var local_medicine : Int,

    var brand_name : String?

)

fun FailureSalesOderMedicineEntity.toSaleOderMedicine() : SalesOderMedicine {
    return SalesOderMedicine(
        room_id = room_id,
        unit = unit,
        quantity = quantity,
        local_medicine = local_medicine,
        brand_name = brand_name
    )
}