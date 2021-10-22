package com.devscore.digital_pharmacy.business.datasource.cache.customer

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.domain.models.Customer


@Entity(tableName = "FailureAppClientCustomer")
data class FailureCustomerEntity (


    @PrimaryKey(autoGenerate = true)
    var room_id : Long? = -1,
    var name : String?,
    var email : String?,
    var mobile : String?,
    var whatsapp : String?,
    var facebook : String?,
    var imo : String?,
    var address : String?,
    var date_of_birth : String?,
    var total_balance : Int?,
    var due_balance : Int?
)


fun FailureCustomerEntity.toCustomer() : Customer {
    return Customer(
        room_id = room_id,
        name = name,
        email = email,
        mobile = mobile,
        whatsapp = whatsapp,
        facebook = facebook,
        imo = imo,
        address = address,
        date_of_birth = date_of_birth,
        total_balance = total_balance,
        due_balance = due_balance
    )
}