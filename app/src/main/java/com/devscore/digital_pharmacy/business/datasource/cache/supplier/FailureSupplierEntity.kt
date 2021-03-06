package com.devscore.digital_pharmacy.business.datasource.cache.supplier

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.domain.models.Supplier

@Entity(tableName = "FailureAppClientVendor")
data class FailureSupplierEntity (

    @PrimaryKey(autoGenerate = true)
    var room_id : Long? = -1,
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
)


fun FailureSupplierEntity.toSupplier() : Supplier {
    return Supplier(
        room_id = room_id,
        company_name = company_name,
        agent_name = agent_name,
        email = email,
        mobile = mobile,
        whatsapp = whatsapp,
        facebook = facebook,
        imo = imo,
        address = address,
        total_balance = total_balance,
        due_balance = due_balance
    )
}