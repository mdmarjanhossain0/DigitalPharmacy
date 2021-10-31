package com.devscore.digital_pharmacy.business.datasource.cache.purchases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.domain.models.PurchasesOrderMedicine

@Entity(
    tableName = "PurchasesOrderMedicine",
    foreignKeys = [
        ForeignKey(
            entity = PurchasesOrderEntity::class,
            parentColumns = ["pk"],
            childColumns = ["purchases_order"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PurchasesOrderMedicineEntity (



    @ColumnInfo(name = "purchases_order", index = true)
    var purchases_order : Int,

    @PrimaryKey(autoGenerate = false)
    var pk : Int?,

    var unit : Int,

    var quantity : Float,

    var local_medicine : Int,

    var brand_name : String?

)

fun PurchasesOrderMedicineEntity.toPurchasesOrderMedicine() : PurchasesOrderMedicine {
    return PurchasesOrderMedicine (
        pk = pk,
        unit = unit,
        quantity = quantity,
        local_medicine = local_medicine,
        brand_name = brand_name
    )
}