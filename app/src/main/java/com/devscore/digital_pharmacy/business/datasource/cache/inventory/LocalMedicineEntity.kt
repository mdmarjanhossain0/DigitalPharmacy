package com.devscore.digital_pharmacy.business.datasource.cache.inventory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.datasource.network.inventory.LocalMedicineDto
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine

@Entity(tableName = "LocalMedicine")
data class LocalMedicineEntity (

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    var id : Int,

    @ColumnInfo(name = "name")
    var name : String,

    @ColumnInfo(name = "sku")
    var sku : String,

    @ColumnInfo(name = "dar_number")
    var darNumber : String,


    @ColumnInfo(name = "mr_number")
    var mrNumber : String,


    @ColumnInfo(name = "generic")
    var generic : String,

    @ColumnInfo(name = "indication")
    var indication : String,

    @ColumnInfo(name = "symptom")
    var symptom : String,

    @ColumnInfo(name = "strength")
    var strength : String,

    @ColumnInfo(name = "description")
    var description : String,

    @ColumnInfo(name = "base_mrp")
    var baseMrp : String,

    @ColumnInfo(name = "discount")
    var discount : String,

    @ColumnInfo(name = "id_percent_discount")
    var isPercentDiscount : Boolean,

    @ColumnInfo(name = "sales_unit")
    var salesUnit : String,

    @ColumnInfo(name = "purchase_unit")
    var purchasesUnit : String,

    @ColumnInfo(name = "bramd")
    var brand : String,

    @ColumnInfo(name = "manufacture")
    var manufacture : String,

    @ColumnInfo(name = "kind")
    var kind : String,

    @ColumnInfo(name = "form")
    var form : String,

    @ColumnInfo(name = "remaining_quantity")
    var remainingQuantity : String,

    @ColumnInfo(name = "damage_quantity")
    var damageQuantity : String,

    @ColumnInfo(name = "sales_price")
    var salePrice : Int,

    @ColumnInfo(name = "purchases_price")
    var purchasePrice : Int,

    @ColumnInfo(name = "rack_number")
    var rackNumber : String

)

fun LocalMedicineEntity.toLocalMedicine() : LocalMedicine {
    return LocalMedicine(
        id = id,
        name = name,
        sku = sku,
        darNumber = darNumber,
        mrNumber = mrNumber,
        generic = generic,
        indication = indication,
        symptom = symptom,
        strength = strength,
        description = description,
        baseMrp = baseMrp,
        discount = discount,
        isPercentDiscount = isPercentDiscount,
        salesUnit = salesUnit,
        purchasesUnit = purchasesUnit,
        brand = brand,
        manufacture = manufacture,
        kind = kind,
        form = form,
        remainingQuantity = remainingQuantity,
        damageQuantity = damageQuantity,
        salePrice = salePrice,
        purchasePrice = purchasePrice,
        rackNumber = rackNumber
    )
}


fun LocalMedicine.toLocalMedicineEntity() : LocalMedicineEntity {
    return LocalMedicineEntity(
        id = id,
        name = name,
        sku = sku,
        darNumber = darNumber,
        mrNumber = mrNumber,
        generic = generic,
        indication = indication,
        symptom = symptom,
        strength = strength,
        description = description,
        baseMrp = baseMrp,
        discount = discount,
        isPercentDiscount = isPercentDiscount,
        salesUnit = salesUnit,
        purchasesUnit = purchasesUnit,
        brand = brand,
        manufacture = manufacture,
        kind = kind,
        form = form,
        remainingQuantity = remainingQuantity,
        damageQuantity = damageQuantity,
        salePrice = salePrice,
        purchasePrice = purchasePrice,
        rackNumber = rackNumber
    )
}