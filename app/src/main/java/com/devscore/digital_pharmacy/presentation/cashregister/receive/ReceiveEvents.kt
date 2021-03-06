package com.devscore.digital_pharmacy.presentation.cashregister.receive

import com.devscore.digital_pharmacy.business.domain.models.Customer
import com.devscore.digital_pharmacy.business.domain.models.Receive
import com.devscore.digital_pharmacy.business.domain.models.Supplier
import com.devscore.digital_pharmacy.business.domain.util.StateMessage

sealed class ReceiveEvents {

    object NewReceiveCreate : ReceiveEvents()

    data class CacheState(val receive : Receive): ReceiveEvents()

    data class AddCustomer(val customer : Customer): ReceiveEvents()


    data class AddSupplier(val supplier : Supplier): ReceiveEvents()

    data class Error(val stateMessage: StateMessage): ReceiveEvents()

    object OnRemoveHeadFromQueue: ReceiveEvents()
}