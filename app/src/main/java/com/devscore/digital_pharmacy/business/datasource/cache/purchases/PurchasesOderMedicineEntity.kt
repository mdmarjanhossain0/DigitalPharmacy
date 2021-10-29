package com.devscore.digital_pharmacy.business.datasource.cache.purchases

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.domain.models.PurchasesOderMedicine

@Entity(
    tableName = "PurchasesOderMedicine",
    foreignKeys = [
        ForeignKey(
            entity = PurchasesOderEntity::class,
            parentColumns = ["pk"],
            childColumns = ["purchases_oder"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PurchasesOderMedicineEntity (


    var purchases_oder : Int,

    @PrimaryKey(autoGenerate = false)
    var pk : Int?,

    var unit : Int,

    var quantity : Long,

    var local_medicine : Int,

    var brand_name : String?

)

fun PurchasesOderMedicineEntity.toPurchasesOderMedicine() : PurchasesOderMedicine {
    return PurchasesOderMedicine (
        pk = pk,
        unit = unit,
        quantity = quantity,
        local_medicine = local_medicine,
        brand_name = brand_name
    )
}