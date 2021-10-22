package com.devscore.digital_pharmacy.business.datasource.network.supplier

import com.devscore.digital_pharmacy.business.datasource.network.supplier.network_response.SupplierCreateResponse
import com.devscore.digital_pharmacy.business.domain.models.Supplier
import com.google.gson.annotations.SerializedName

data class SupplierDto (
    @SerializedName("pk")
    var pk : Int,

    @SerializedName("company_name")
    var company_name : String?,

    @SerializedName("agent_name")
    var agent_name : String?,

    @SerializedName("email")
    var email : String?,

    @SerializedName("mobile")
    var mobile : String?,

    @SerializedName("whatsapp")
    var whatsapp : String?,

    @SerializedName("facebook")
    var facebook : String?,

    @SerializedName("imo")
    var imo : String?,

    @SerializedName("address")
    var address : String?,

    @SerializedName("created_at")
    var created_at : String?,

    @SerializedName("updated_at")
    var updated_at : String?
)

fun SupplierDto.toSupplier() : Supplier {
    return Supplier(
        pk = pk,
        company_name = company_name,
        agent_name = agent_name,
        email = email,
        mobile = mobile,
        whatsapp = whatsapp,
        facebook = facebook,
        imo = imo,
        address = address,
        created_at = created_at,
        updated_at = updated_at
    )
}