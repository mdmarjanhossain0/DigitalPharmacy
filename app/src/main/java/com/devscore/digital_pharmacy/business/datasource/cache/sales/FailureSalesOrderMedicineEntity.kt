package com.devscore.digital_pharmacy.business.datasource.cache.sales

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.domain.models.SalesOrderMedicine

@Entity(
    tableName = "FailureSalesOrderMedicine",
    foreignKeys = [
        ForeignKey(
            entity = FailureSalesOrderEntity::class,
            parentColumns = ["room_id"],
            childColumns = ["sales_order"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FailureSalesOrderMedicineEntity (


    @ColumnInfo(name = "sales_order", index = true)
    var sales_order : Long?,


    @PrimaryKey(autoGenerate = true)
    var room_id : Long? = -1,

    var unit : Int,

    var quantity : Float,

    var local_medicine : Int,

    var brand_name : String?,

    var unit_name : String?,

    var amount : Float?

)

fun FailureSalesOrderMedicineEntity.toSaleOrderMedicine() : SalesOrderMedicine {
    return SalesOrderMedicine(
        room_id = room_id,
        unit = unit,
        quantity = quantity,
        local_medicine = local_medicine,
        brand_name = brand_name,
        unit_name = unit_name,
        amount = amount
    )
}