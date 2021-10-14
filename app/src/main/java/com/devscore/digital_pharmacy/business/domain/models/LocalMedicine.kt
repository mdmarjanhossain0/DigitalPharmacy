package com.devscore.digital_pharmacy.business.domain.models

import com.google.gson.annotations.SerializedName

data class LocalMedicine (
    var id : Int,

    var name : String,

    var sku : String, var darNumber : String,

    var mrNumber : String,

    var generic : String,

    var indication : String,

    var symptom : String,

    var strength : String,

    var description : String,

    var baseMrp : String,

    var discount : String,

    var isPercentDiscount : Boolean,

    var salesUnit : String,

    var purchasesUnit : String,

    var brand : String,

    var manufacture : String,

    var kind : String,

    var form : String,

    var remainingQuantity : String,

    var damageQuantity : String,

    var salePrice : Int,

    var purchasePrice : Int,

    var rackNumber : String

)