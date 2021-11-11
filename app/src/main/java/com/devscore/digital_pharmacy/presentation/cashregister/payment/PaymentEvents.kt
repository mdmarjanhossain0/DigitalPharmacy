package com.devscore.digital_pharmacy.presentation.cashregister.payment

import com.devscore.digital_pharmacy.business.domain.models.Customer
import com.devscore.digital_pharmacy.business.domain.models.Payment
import com.devscore.digital_pharmacy.business.domain.models.Receive
import com.devscore.digital_pharmacy.business.domain.models.Supplier
import com.devscore.digital_pharmacy.business.domain.util.StateMessage
import com.devscore.digital_pharmacy.presentation.cashregister.receive.ReceiveEvents

sealed class PaymentEvents {

    object NewPaymentCreate : PaymentEvents()

    data class CacheState(val payment : Payment): PaymentEvents()

    data class AddCustomer(val customer : Customer): PaymentEvents()


    data class AddSupplier(val supplier : Supplier): PaymentEvents()

    data class Error(val stateMessage: StateMessage): PaymentEvents()

    object OnRemoveHeadFromQueue: PaymentEvents()
}