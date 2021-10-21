package com.devscore.digital_pharmacy.business.datasource.cache.supplier

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.domain.models.Supplier

@Entity(tableName = "AppClientVendor")
data class SupplierEntity (

    @PrimaryKey(autoGenerate = false)
    var pk : Int,
    var company_name : String?,
    var agent_name : String?,
    var mobile : String?,
    var whatsapp : String?,
    var facebook : String?,
    var imo : String?,
    var address : String?,
    var created_at : String?,
    var updated_at : String?,
)


fun SupplierEntity.toSupplier() : Supplier {
    return Supplier(
        pk = pk,
        company_name = company_name,
        agent_name = agent_name,
        mobile = mobile,
        whatsapp = whatsapp,
        facebook = facebook,
        imo = imo,
        address = address,
        created_at = created_at,
        updated_at = updated_at
    )
}