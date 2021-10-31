package com.devscore.digital_pharmacy.presentation.purchases.orderlist

import com.devscore.digital_pharmacy.business.domain.util.StateMessage

sealed class PurchasesOrderListEvents {

    object CreateNewOrder : PurchasesOrderListEvents()

    data class SearchWithQuery(val query: String) : PurchasesOrderListEvents()

    object NextPage: PurchasesOrderListEvents()

    data class UpdateQuery(val query: String): PurchasesOrderListEvents()


    object GetOrderAndFilter: PurchasesOrderListEvents()

    data class Error(val stateMessage: StateMessage): PurchasesOrderListEvents()

    object OnRemoveHeadFromQueue: PurchasesOrderListEvents()
}