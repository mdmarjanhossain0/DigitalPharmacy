package com.devscore.digital_pharmacy.business.datasource.network.sales.network_response

import com.devscore.digital_pharmacy.business.domain.models.SalesOder
import com.google.gson.annotations.SerializedName

data class CreateSalesOderResponse (

    @SerializedName("pk") var pk : Int?,
    @SerializedName("customer") var customer : Int,
    @SerializedName("total_amount") var total_amount : Long?,
    @SerializedName("total_after_discount") var total_after_discount : Long?,
    @SerializedName("paid_amount") var paid_amount : Long?,
    @SerializedName("discount") var discount : Long?,
    @SerializedName("is_discount_percent") var is_discount_percent : Boolean,
    @SerializedName("sales_oder_medicines") var sales_oder_medicines : List<SalesOderItemDto>,
    @SerializedName("created_at")var created_at : String,
    @SerializedName("updated_at")var updated_at : String?,
    @SerializedName("brand_name") var brand_name : String?

)



fun CreateSalesOderResponse.toSalesOder() : SalesOder {
    return SalesOder(
        pk = pk,
        customer = customer,
        total_amount = total_amount,
        total_after_discount = total_after_discount,
        paid_amount = paid_amount,
        discount = discount,
        is_discount_percent = is_discount_percent,
        created_at = created_at,
        updated_at = updated_at,
        sales_oder_medicines = sales_oder_medicines.map {
            it.toSalesOderMedicine()
        }
    )
}