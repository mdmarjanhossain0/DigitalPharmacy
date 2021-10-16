package com.devscore.digital_pharmacy.business.datasource.cache.inventory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.domain.models.GlobalMedicine


@Entity(tableName = "GlobalMedicine")
data class GlobalMedicineEntity (

    @ColumnInfo(name="id")
    @PrimaryKey
    val id : Int,

    @ColumnInfo(name="name")
    val name : String,

    @ColumnInfo(name="sku")
    val sku : String?,

    @ColumnInfo(name="dr_number")
    val darNumber : String?,

    @ColumnInfo(name="mr_number")
    val mrNumber : String?,

    @ColumnInfo(name="generic")
    val generic : String?,

    @ColumnInfo(name="indication")
    val indication : String?,

    @ColumnInfo(name="symptom")
    val symptom : String?,

    @ColumnInfo(name="strength")
    val strength : String?,

    @ColumnInfo(name="description")
    val description : String?,

    @ColumnInfo(name="baseMrp")
    val mrp : Int?,

    @ColumnInfo(name="basePurchasePrice")
    val purchases_price : Int?,

    @ColumnInfo(name="manufacture")
    val manufacture : String?,

    @ColumnInfo(name="kind")
    val kind : String?,

    @ColumnInfo(name="form")
    val form : String?,

    val currentDateTime : Long = System.currentTimeMillis(),

    @ColumnInfo(name="createdAt")
    val createdAt : String?,

    @ColumnInfo(name="updatedAt")
    val updatedAt : String?
)

fun GlobalMedicineEntity.toGlobalMedicine() : GlobalMedicine {
    return GlobalMedicine(
        id = id,
        brand_name = name,
        sku = sku,
        darNumber = darNumber,
        mrNumber = mrNumber,
        generic = generic,
        indication = indication,
        symptom = symptom,
        strength = strength,
        description = description,
        mrp = mrp,
        purchases_price = purchases_price,
        manufacture = manufacture,
        kind = kind,
        form = form,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun GlobalMedicine.toGlobalMedicineEntity() : GlobalMedicineEntity {
    return GlobalMedicineEntity(
        id = id,
        name = brand_name,
        sku = sku,
        darNumber = darNumber,
        mrNumber = mrNumber,
        generic = generic,
        indication = indication,
        symptom = symptom,
        strength = strength,
        description = description,
        mrp = mrp,
        purchases_price = purchases_price,
        manufacture = manufacture,
        kind = kind,
        form = form,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}