package com.devscore.digital_pharmacy.business.domain.models

import com.devscore.digital_pharmacy.business.datasource.cache.cashregister.FailurePaymentEntity
import com.devscore.digital_pharmacy.business.datasource.cache.cashregister.FailureReceiveEntity
import com.devscore.digital_pharmacy.business.datasource.cache.cashregister.PaymentEntity
import com.devscore.digital_pharmacy.business.datasource.cache.cashregister.ReceiveEntity

data class Payment (
    var pk : Int? = null,
    var room_id : Long? = null,
    var date : String,
    var customer : Int?,
    var vendor : Int?,
    var type : String,
    var amount : Float,
    var balance : Float,
    var remarks : String?,
    var created_at : String? = null,
    var updated_at : String? = null,
    var customer_name : String? = null,
    var vendor_name : String? = null
)





fun Payment.toPaymentEntity() : PaymentEntity {
    return PaymentEntity(
        pk = pk!!,
        date = date,
        customer = customer,
        vendor = vendor,
        type = type,
        amount = amount,
        balance = balance,
        remarks = remarks!!,
        created_at = created_at!!,
        updated_at = updated_at!!,
        customer_name = customer_name,
        vendor_name = vendor_name!!
    )
}


fun Payment.toFailurePayment() : FailurePaymentEntity {
    return FailurePaymentEntity(
        date = date,
        customer = customer,
        vendor = vendor,
        type = type,
        amount = amount,
        balance = balance,
        remarks = remarks!!
    )
}


fun Payment.toCreatePayment() : CreatePayment {
    return CreatePayment(
        date = date,
        customer = customer,
        vendor = vendor,
        type = type,
        amount = amount,
        balance = balance,
        remarks = remarks!!
    )
}