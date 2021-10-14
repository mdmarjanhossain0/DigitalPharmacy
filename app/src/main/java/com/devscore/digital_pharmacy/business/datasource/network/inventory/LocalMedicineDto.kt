package com.devscore.digital_pharmacy.business.datasource.network.inventory

import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.google.gson.annotations.SerializedName

data class LocalMedicineDto (

    @SerializedName("id") var id : Int,
    @SerializedName("name") var name : String,
    @SerializedName("sku") var sku : String,
    @SerializedName("dar_number") var darNumber : String,
    @SerializedName("mr_number") var mrNumber : String,
    @SerializedName("generic") var generic : String,
    @SerializedName("indication") var indication : String,
    @SerializedName("symptom") var symptom : String,
    @SerializedName("strength") var strength : String,
    @SerializedName("description") var description : String,
    @SerializedName("base_mrp") var baseMrp : String,
    @SerializedName("discount") var discount : String,
    @SerializedName("is_percent_discount") var isPercentDiscount : Boolean,
    @SerializedName("sales_unit") var salesUnit : String,
    @SerializedName("purchases_unit") var purchasesUnit : String,
    @SerializedName("brand") var brand : String,
    @SerializedName("manufacture") var manufacture : String,
    @SerializedName("kind") var kind : String,
    @SerializedName("form") var form : String,
    @SerializedName("remaining_quanity") var remainingQuantity : String,
    @SerializedName("damage_quantity") var damageQuantity : String,
    @SerializedName("sale_price") var salePrice : Int,
    @SerializedName("purchase_price") var purchasePrice : Int,
    @SerializedName("rack_number") var rackNumber : String

)


fun LocalMedicineDto.toLocalMedicine() : LocalMedicine {
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