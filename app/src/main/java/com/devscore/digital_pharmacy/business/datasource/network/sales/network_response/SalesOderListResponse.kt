package com.devscore.digital_pharmacy.business.datasource.network.sales.network_response

import com.devscore.digital_pharmacy.business.datasource.network.inventory.LocalMedicineDto
import com.devscore.digital_pharmacy.business.datasource.network.inventory.toLocalMedicine
import com.devscore.digital_pharmacy.business.datasource.network.sales.SalesOderDto
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.google.gson.annotations.SerializedName

data class SalesOderListResponse (

    @SerializedName("count") var count : Int?,
    @SerializedName("next") var next : String?,
    @SerializedName("previous") var previous : String?,
    @SerializedName("results") var results : List<SalesOderDto>

)