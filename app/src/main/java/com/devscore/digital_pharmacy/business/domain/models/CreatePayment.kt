package com.devscore.digital_pharmacy.business.domain.models


data class CreatePayment (
    var date : String,
    var customer : Int?,
    var vendor : Int?,
    var type : String,
    var amount : Float,
    var balance : Float,
    var remarks : String
)


fun CreatePayment.toPayment() : Payment {
    return Payment(
        date = date,
        customer = customer,
        vendor = vendor,
        type = type,
        amount = amount,
        balance = balance,
        remarks = remarks,
    )
}