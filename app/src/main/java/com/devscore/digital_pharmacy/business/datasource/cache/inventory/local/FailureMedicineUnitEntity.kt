package com.devscore.digital_pharmacy.business.datasource.cache.inventory.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "FailureMedicineUnit",
    foreignKeys = [
        ForeignKey(
            entity = FailureMedicineEntity::class,
            parentColumns = ["room_medicine_id"],
            childColumns = ["medicine_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FailureMedicineUnitEntity (
    var medicine_id : Int,

    @PrimaryKey(autoGenerate = true)
    var room_id : Long? = -1,

    var quantity : Int,
    var name : String,
    var type : String
)