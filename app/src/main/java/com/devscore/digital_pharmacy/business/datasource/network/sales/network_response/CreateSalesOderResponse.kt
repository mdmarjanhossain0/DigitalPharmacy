package com.devscore.digital_pharmacy.business.datasource.network.sales.network_response

import com.google.gson.annotations.SerializedName

data class CreateSalesOderResponse (

    @SerializedName("pk") var pk : Int?,
    @SerializedName("customer") var customer : Int,
    @SerializedName("total_amount") var totalAmount : String,
    @SerializedName("total_after_discount") var totalAfterDiscount : String,
    @SerializedName("paid_amount") var paidAmount : String,
    @SerializedName("discount") var discount : String,
    @SerializedName("is_discount_percent") var isDiscountPercent : Boolean,
    @SerializedName("oder_items") var oderItems : List<SalesOderItem>,
    @SerializedName("created_at")var created_at : String,
    @SerializedName("updated_at")var updated_at : String?

)