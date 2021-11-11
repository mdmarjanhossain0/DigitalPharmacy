package com.devscore.digital_pharmacy.presentation.purchases.orderlist

import com.devscore.digital_pharmacy.business.domain.models.PurchasesOrder
import com.devscore.digital_pharmacy.business.domain.models.SalesOrder
import com.devscore.digital_pharmacy.business.domain.util.Queue
import com.devscore.digital_pharmacy.business.domain.util.StateMessage

data class PurchasesOrderListState (
    val isLoading : Boolean = false,
    val orderList : List<PurchasesOrder> = listOf(),
    val query: String = "",
    val page: Int = 1,
    val isQueryExhausted: Boolean = false, // no more results available, prevent next page
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)