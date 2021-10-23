package com.devscore.digital_pharmacy.business.domain.models

import com.google.gson.annotations.SerializedName

data class SalesOderMedicine (

    var unit : Int,
    var quantity : Long,
    var local_medicine : Int

)