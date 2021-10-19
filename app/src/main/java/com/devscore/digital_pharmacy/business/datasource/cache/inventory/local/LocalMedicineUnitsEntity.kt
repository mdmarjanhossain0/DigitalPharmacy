package com.devscore.digital_pharmacy.business.datasource.cache.inventory.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "MedicineUnit",
    foreignKeys = [
        ForeignKey(
            entity = LocalMedicineEntity::class,
            parentColumns = ["room_medicine_id"],
            childColumns = ["medicine_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LocalMedicineUnitsEntity (
    var medicine_id : Int,

//    @ColumnInfo(name = "room_unit_id")
//    @PrimaryKey(autoGenerate = true)
//    var room_unit_id : Int? = null,

    @PrimaryKey(autoGenerate = false)
    var id : Int,
    var quantity : Int,
    var name : String,
    var type : String
    )