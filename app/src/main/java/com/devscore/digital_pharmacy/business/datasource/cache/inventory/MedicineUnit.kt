package com.devscore.digital_pharmacy.business.datasource.cache.inventory

import androidx.room.Entity

@Entity(tableName = "MedicineUnit")
data class MedicineUnit (
    var id : Int,
    var quantity : Int,
    var name : String,
    var type : String
        )