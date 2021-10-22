package com.devscore.digital_pharmacy.business.domain.models

import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.datasource.cache.supplier.FailureSupplierEntity
import com.devscore.digital_pharmacy.business.datasource.cache.supplier.SupplierEntity

data class Supplier (
    var pk : Int? = null,
    var room_id : Long? = 1,
    var company_name : String?,
    var agent_name : String?,
    var email : String?,
    var mobile : String?,
    var whatsapp : String?,
    var facebook : String?,
    var imo : String?,
    var address : String?,
    var created_at : String? = null,
    var updated_at : String? = null,
)


fun Supplier.toSupplierEntity() : SupplierEntity {
    return SupplierEntity(
        pk = pk!!,
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


fun Supplier.toFailureSupplierEntity() : FailureSupplierEntity {
    return FailureSupplierEntity(
        company_name = company_name,
        agent_name = agent_name,
        email = email,
        mobile = mobile,
        whatsapp = whatsapp,
        facebook = facebook,
        imo = imo,
        address = address
    )
}