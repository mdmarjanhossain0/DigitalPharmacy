package com.devscore.digital_pharmacy.business.datasource.network.customer.network_response

import com.devscore.digital_pharmacy.business.domain.models.Customer

data class CustomerNetworkResponse (
    var pk : Int,
    var name : String?,
    var email : String?,
    var mobile : String?,
    var whatsapp : String?,
    var facebook : String?,
    var imo : String?,
    var address : String?,
    var date_of_birth : String?,
    var created_at : String?,
    var updated_at : String?,


    var response : String?,
    var errorMessage : String?
)


fun CustomerNetworkResponse.toCustomer() : Customer {
    return Customer(
        pk = pk,
        name = name,
        email = email,
        mobile = mobile,
        whatsapp = whatsapp,
        facebook = facebook,
        imo = imo,
        address = address,
        date_of_birth = date_of_birth,
        created_at = created_at,
        updated_at = updated_at
    )
}