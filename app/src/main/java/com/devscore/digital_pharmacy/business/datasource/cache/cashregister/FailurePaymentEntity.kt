package com.devscore.digital_pharmacy.business.datasource.cache.cashregister

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.domain.models.Payment
import com.devscore.digital_pharmacy.business.domain.models.Receive



@Entity(tableName = "FailurePayment")
data class FailurePaymentEntity (

    @PrimaryKey(autoGenerate = true)
    var room_id : Long = -1,
    var date : String,
    var customer : Int?,
    var vendor : Int?,
    var type : String,
    var amount : Float,
    var balance : Float,
    var remarks : String
)


fun FailurePaymentEntity.toPayment() : Payment {
    return Payment(
        room_id = room_id,
        date = date,
        customer = customer,
        vendor = vendor,
        type = type,
        amount = amount,
        balance = balance,
        remarks = remarks
    )
}