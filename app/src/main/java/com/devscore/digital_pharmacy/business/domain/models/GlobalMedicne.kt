package com.devscore.digital_pharmacy.business.domain.models

import com.google.gson.annotations.SerializedName

data class GlobalMedicine (
    var id : Int,
    var name : String,
    var sku : String,
    var darNumber : String,
    var mrNumber : String,
    var generic : String,
    var indication : String,
    var symptom : String,
    var strength : String,
    var description : String,
    var baseMrp : Int,
    var basePurchasePrice : Int,
    var brand : String,
    var manufacture : String,
    var kind : String,
    var form : String,
    var createdAt : String,
    var updatedAt : String
)