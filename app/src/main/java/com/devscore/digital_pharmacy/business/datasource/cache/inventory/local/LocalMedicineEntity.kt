package com.devscore.digital_pharmacy.business.datasource.cache.inventory.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine

@Entity(tableName = "LocalMedicine")
data class LocalMedicineEntity (

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    var id : Int,

    @ColumnInfo(name = "brand_name")
    var brand_name : String?,

    @ColumnInfo(name = "sku")
    var sku : String?,

    @ColumnInfo(name = "dar_number")
    var dar_number : String?,


    @ColumnInfo(name = "mr_number")
    var mr_number : String?,


    @ColumnInfo(name = "generic")
    var generic : String?,

    @ColumnInfo(name = "indication")
    var indication : String?,

    @ColumnInfo(name = "symptom")
    var symptom : String?,

    @ColumnInfo(name = "strength")
    var strength : String?,

    @ColumnInfo(name = "description")
    var description : String?,

    @ColumnInfo(name = "base_mrp")
    var mrp : Int?,

    @ColumnInfo(name = "purchases_price")
    var purchases_price : Int?,

    @ColumnInfo(name = "discount")
    var discount : Int?,

    @ColumnInfo(name = "id_percent_discount")
    var is_percent_discount : Boolean,

    @ColumnInfo(name = "manufacture")
    var manufacture : String?,

    @ColumnInfo(name = "kind")
    var kind : String?,

    @ColumnInfo(name = "form")
    var form : String?,

    @ColumnInfo(name = "remaining_quantity")
    var remaining_quantity : Int?,

    @ColumnInfo(name = "damage_quantity")
    var damage_quantity : Int?,


    @ColumnInfo(name = "rack_number")
    var rack_number : String?

)