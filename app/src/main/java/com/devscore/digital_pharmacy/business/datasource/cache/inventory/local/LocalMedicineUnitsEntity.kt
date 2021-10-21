package com.devscore.digital_pharmacy.business.datasource.cache.inventory.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "MedicineUnit",
    foreignKeys = [
        ForeignKey(
            entity = LocalMedicineEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicine_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LocalMedicineUnitsEntity (
    var medicine_id : Int,

    @PrimaryKey(autoGenerate = false)
    var id : Int,
    var quantity : Int,
    var name : String,
    var type : String
    )