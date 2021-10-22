package com.devscore.digital_pharmacy.business.domain.models

import com.devscore.digital_pharmacy.business.datasource.cache.customer.CustomerEntity
import com.devscore.digital_pharmacy.business.datasource.cache.customer.FailureCustomerEntity

data class Customer (
    var pk : Int? = -1,
    var room_id : Long? = -1,
    var name : String?,
    var email : String?,
    var mobile : String?,
    var whatsapp : String?,
    var facebook : String?,
    var imo : String?,
    var address : String?,
    var date_of_birth : String?,
    var created_at : String? = null,
    var updated_at : String? = null,
)


fun Customer.toCustomerEntity() : CustomerEntity {
    return CustomerEntity(
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

fun Customer.toCustomerFailureEntity() : FailureCustomerEntity {
    return FailureCustomerEntity(
        name = name,
        email = email,
        mobile = mobile,
        whatsapp = whatsapp,
        facebook = facebook,
        imo = imo,
        address = address,
        date_of_birth = date_of_birth,
    )
}