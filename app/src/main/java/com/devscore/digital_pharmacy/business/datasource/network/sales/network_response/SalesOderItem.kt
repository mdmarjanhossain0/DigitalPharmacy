package com.devscore.digital_pharmacy.business.datasource.network.sales.network_response

import com.google.gson.annotations.SerializedName

data class SalesOderItem (

    @SerializedName("unit") var unit : Int,
    @SerializedName("quantity") var quantity : String,
    @SerializedName("local_medicine") var local_medicine : Int

)