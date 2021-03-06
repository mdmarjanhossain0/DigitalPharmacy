package com.devscore.digital_pharmacy.business.datasource.cache.cashregister

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.domain.models.Payment

@Entity(tableName = "Payment")
data class PaymentEntity (

    @PrimaryKey(autoGenerate = false)
    var pk : Int,
    var date : String,
    var customer : Int?,
    var vendor : Int?,
    var type : String,
    var amount : Float,
    var balance : Float,
    var remarks : String,
    var created_at : String,
    var updated_at : String,
    var customer_name : String?,
    var vendor_name : String
)


fun PaymentEntity.toPayment() : Payment {
    return Payment(
        pk = pk,
        date = date,
        customer = customer,
        vendor = vendor,
        type = type,
        amount = amount,
        balance = balance,
        remarks = remarks,
        created_at = created_at,
        updated_at = updated_at,
        customer_name = customer_name,
        vendor_name = vendor_name
    )
}