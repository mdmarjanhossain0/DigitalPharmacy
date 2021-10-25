package com.devscore.digital_pharmacy.presentation.sales.orderlist

import com.devscore.digital_pharmacy.business.domain.util.StateMessage

sealed class SalesOrderListEvents {

    object NewSalesOrderListSearch : SalesOrderListEvents()

    data class SearchWithQuery(val query: String) : SalesOrderListEvents()

    object NextPage: SalesOrderListEvents()

    data class UpdateQuery(val query: String): SalesOrderListEvents()


    object GetOrderAndFilter: SalesOrderListEvents()

    data class Error(val stateMessage: StateMessage): SalesOrderListEvents()

    object OnRemoveHeadFromQueue: SalesOrderListEvents()
}