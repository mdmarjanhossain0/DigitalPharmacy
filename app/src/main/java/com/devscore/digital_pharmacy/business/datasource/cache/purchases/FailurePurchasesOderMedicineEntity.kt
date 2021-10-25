package com.devscore.digital_pharmacy.business.datasource.cache.purchases

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.domain.models.PurchasesOderMedicine

@Entity(
    tableName = "FailurePurchasesOderMedicine",
    foreignKeys = [
        ForeignKey(
            entity = FailurePurchasesOderEntity::class,
            parentColumns = ["room_id"],
            childColumns = ["purchases_oder"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FailurePurchasesOderMedicineEntity (


    var purchases_oder : Int,


    @PrimaryKey(autoGenerate = true)
    var room_id : Long? = -1,

    var unit : Int,

    var quantity : Long,

    var local_medicine : Int,

    var brand_name : String?

)

fun FailurePurchasesOderMedicineEntity.toPurchasesOderMedicine() : PurchasesOderMedicine {
    return PurchasesOderMedicine (
        room_id = room_id,
        unit = unit,
        quantity = quantity,
        local_medicine = local_medicine,
        brand_name = brand_name
    )
}