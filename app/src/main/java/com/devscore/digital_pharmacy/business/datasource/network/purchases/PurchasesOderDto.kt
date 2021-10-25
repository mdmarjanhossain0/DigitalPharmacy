package com.devscore.digital_pharmacy.business.datasource.network.purchases

import com.devscore.digital_pharmacy.business.datasource.network.purchases.network_response.PurchasesOderItemDto
import com.devscore.digital_pharmacy.business.datasource.network.purchases.network_response.toPurchasesOderMedicine
import com.devscore.digital_pharmacy.business.datasource.network.sales.network_response.SalesOderItemDto
import com.devscore.digital_pharmacy.business.datasource.network.sales.network_response.toSalesOderMedicine
import com.devscore.digital_pharmacy.business.domain.models.PurchasesOder
import com.devscore.digital_pharmacy.business.domain.models.SalesOder
import com.google.gson.annotations.SerializedName

data class PurchasesOderDto (
    @SerializedName("pk") var pk : Int?,
    @SerializedName("vendor") var vendor : Int,
    @SerializedName("total_amount") var total_amount : Long?,
    @SerializedName("total_after_discount") var total_after_discount : Long?,
    @SerializedName("paid_amount") var paid_amount : Long?,
    @SerializedName("discount") var discount : Long?,
    @SerializedName("is_discount_percent") var is_discount_percent : Boolean,
    @SerializedName("sales_oder_medicines") var purchases_oder_medicines : List<PurchasesOderItemDto>,
    @SerializedName("created_at")var created_at : String,
    @SerializedName("updated_at")var updated_at : String?,
    @SerializedName("brand_name") var brand_name : String?,
    @SerializedName("company") var company : String?
)


fun PurchasesOderDto.toPurchasesOder() : PurchasesOder {
    return PurchasesOder(
        pk = pk,
        vendor = vendor,
        total_amount = total_amount,
        total_after_discount = total_after_discount,
        paid_amount = paid_amount,
        discount = discount,
        is_discount_percent =is_discount_percent,
        created_at = created_at!!,
        updated_at = updated_at,
        purchases_oder_medicine = purchases_oder_medicines.map {
            it.toPurchasesOderMedicine()
        }
    )
}