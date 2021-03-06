package com.devscore.digital_pharmacy.business.datasource.network.sales

import android.util.Log
import com.devscore.digital_pharmacy.business.datasource.network.sales.network_response.SalesOrderItemDto
import com.devscore.digital_pharmacy.business.datasource.network.sales.network_response.toSalesOrderMedicine
import com.devscore.digital_pharmacy.business.domain.models.SalesOrder
import com.google.gson.annotations.SerializedName

data class SalesOrderDto (
    @SerializedName("pk") var pk : Int?,
    @SerializedName("customer") var customer : Int,
    @SerializedName("total_amount") var total_amount : Float?,
    @SerializedName("total_after_discount") var total_after_discount : Float?,
    @SerializedName("paid_amount") var paid_amount : Float?,
    @SerializedName("discount") var discount : Float?,
    @SerializedName("is_discount_percent") var is_discount_percent : Boolean,
    @SerializedName("sales_oder_medicines") var sales_oder_medicines : List<SalesOrderItemDto>,
    @SerializedName("created_at")var created_at : String,
    @SerializedName("updated_at")var updated_at : String?,
    @SerializedName("brand_name") var brand_name : String?,
    @SerializedName("status") var status : Int
        )


fun SalesOrderDto.toSalesOrder() : SalesOrder {
    Log.d("sfsdf", total_after_discount?.javaClass?.name.toString())
    return SalesOrder(
        pk = pk,
        customer = customer,
        total_amount = total_amount,
        total_after_discount = total_after_discount,
        paid_amount = paid_amount,
        discount = discount,
        is_discount_percent =is_discount_percent,
        status = status,
        created_at = created_at,
        updated_at = updated_at,
        sales_oder_medicines = sales_oder_medicines.map {
            it.toSalesOrderMedicine()
        }
    )
}