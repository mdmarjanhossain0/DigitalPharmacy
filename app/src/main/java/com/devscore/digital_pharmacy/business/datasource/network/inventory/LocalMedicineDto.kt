package com.devscore.digital_pharmacy.business.datasource.network.inventory

import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.MedicineUnits
import com.google.gson.annotations.SerializedName

data class LocalMedicineDto (

    @SerializedName("id") var id : Int,
    @SerializedName("name") var brand_name : String,
    @SerializedName("sku") var sku : String?,
    @SerializedName("dar_number") var dar_number : String?,
    @SerializedName("mr_number") var mr_number : String?,
    @SerializedName("generic") var generic : String?,
    @SerializedName("indication") var indication : String?,
    @SerializedName("symptom") var symptom : String?,
    @SerializedName("strength") var strength : String?,
    @SerializedName("description") var description : String?,
    @SerializedName("base_mrp") var mrp : Int?,
    @SerializedName("purchases_price")var purchases_price : Int?,
    @SerializedName("discount") var discount : Int?,
    @SerializedName("is_percent_discount") var is_percent_discount : Boolean,
    @SerializedName("manufacture") var manufacture : String?,
    @SerializedName("kind") var kind : String?,
    @SerializedName("form") var form : String?,
    @SerializedName("remaining_quanity") var remaining_quantity : Int?,
    @SerializedName("damage_quantity") var damage_quantity : Int?,
    @SerializedName("rack_number") var rack_number : String?,
    @SerializedName("units") var units : List<MedicineUnitsDto>?
)

data class MedicineUnitsDto(
    @SerializedName("id")
    var id: Int,

    @SerializedName("quantity")
    var quantity : Int,

    @SerializedName("name")
    var name : String,

    @SerializedName("type")
    var type : String
)


fun LocalMedicineDto.toLocalMedicine() : LocalMedicine {
    return LocalMedicine(
        id = id,
        brand_name = brand_name,
        sku = sku,
        dar_number = dar_number,
        mr_number = mr_number,
        generic = generic,
        indication = indication,
        symptom = symptom,
        strength = strength,
        description = description,
        mrp = mrp,
        purchases_price = purchases_price,
        discount = discount,
        is_percent_discount = is_percent_discount,
        manufacture = manufacture,
        kind = kind,
        form = form,
        remaining_quantity = remaining_quantity,
        damage_quantity = damage_quantity,
        rack_number = rack_number,
        units = units?.map {
            it.toMedicineUnits()
        }
    )
}

fun MedicineUnitsDto.toMedicineUnits(): MedicineUnits {
    return MedicineUnits(
        id = id,
        name = name,
        quantity = quantity,
        type = type
    )
}
