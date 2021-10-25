package com.devscore.digital_pharmacy.business.datasource.network.sales.network_response

import com.devscore.digital_pharmacy.business.datasource.network.inventory.LocalMedicineDto
import com.devscore.digital_pharmacy.business.datasource.network.inventory.network_responses.LocalMedicineResponse
import com.devscore.digital_pharmacy.business.datasource.network.inventory.toLocalMedicine
import com.devscore.digital_pharmacy.business.datasource.network.sales.SalesOderDto
import com.devscore.digital_pharmacy.business.datasource.network.sales.toSalesOder
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.SalesOder
import com.google.gson.annotations.SerializedName

data class SalesOderListResponse (

    @SerializedName("count") var count : Int?,
    @SerializedName("next") var next : String?,
    @SerializedName("previous") var previous : String?,
    @SerializedName("results") var results : List<SalesOderDto>

)

fun SalesOderListResponse.toList() : List<SalesOder> {
    val list : MutableList<SalesOder> = mutableListOf()
    for (dto in results!!) {
        list.add(
            dto.toSalesOder()
        )
    }
    return list
}