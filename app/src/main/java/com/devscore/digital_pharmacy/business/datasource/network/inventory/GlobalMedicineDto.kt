package com.devscore.digital_pharmacy.business.datasource.network.inventory

import com.google.gson.annotations.SerializedName

data class GlobalMedicineDto (

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
    @SerializedName("base_mrp") var baseMrp : Int,
    @SerializedName("base_purchase_price") var basePurchasePrice : Int,
    @SerializedName("brand") var brand : String,
    @SerializedName("manufacture") var manufacture : String,
    @SerializedName("kind") var kind : String,
    @SerializedName("form") var form : String,
    @SerializedName("created_at") var createdAt : String,
    @SerializedName("updated_at") var updatedAt : String

)