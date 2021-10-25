package com.devscore.digital_pharmacy.business.datasource.network.sales.network_response

import com.devscore.digital_pharmacy.business.domain.models.SalesOderMedicine
import com.google.gson.annotations.SerializedName

data class SalesOderItemDto (

    @SerializedName("unit") var unit : Int,
    @SerializedName("quantity") var quantity : Long,
    @SerializedName("local_medicine") var local_medicine : Int,
    @SerializedName("brand_name") var brand_name : String?,
    @SerializedName("pk") var pk : Int?,

    )


fun SalesOderItemDto.toSalesOderMedicine() : SalesOderMedicine {
    return SalesOderMedicine(
        pk = pk,
        unit = unit,
        quantity = quantity,
        local_medicine = local_medicine,
        brand_name = brand_name
    )
}