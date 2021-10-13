package com.devscore.digital_pharmacy.business.datasource.network.inventory.network_responses

import com.devscore.digital_pharmacy.business.datasource.network.inventory.GlobalMedicineDto
import com.google.gson.annotations.SerializedName

data class GlobalResponse (

    @SerializedName("count") var count : Int,
    @SerializedName("next") var next : String,
    @SerializedName("previous") var previous : String,
    @SerializedName("results") var results : List<GlobalMedicineDto>

)