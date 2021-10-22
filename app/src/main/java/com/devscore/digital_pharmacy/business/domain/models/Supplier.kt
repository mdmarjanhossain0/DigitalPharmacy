package com.devscore.digital_pharmacy.business.domain.models

import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.datasource.cache.supplier.FailureSupplierEntity
import com.devscore.digital_pharmacy.business.datasource.cache.supplier.SupplierEntity
import com.google.gson.annotations.SerializedName

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
    var total_balance : Int? = 0,
    var due_balance : Int? = 0,
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
        total_balance = total_balance,
        due_balance = due_balance,
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



fun Supplier.toCreateSupplier() : CreateSupplier {
    return CreateSupplier(
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