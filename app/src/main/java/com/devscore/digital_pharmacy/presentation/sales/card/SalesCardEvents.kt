package com.devscore.digital_pharmacy.presentation.sales.card

import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.util.StateMessage

sealed class SalesCardEvents {

    object GenerateNewOrder : SalesCardEvents()

    object NewLocalMedicineSearch : SalesCardEvents()

    data class AddToCard(val medicine : LocalMedicine): SalesCardEvents()

    data class SearchWithQuery(val query: String) : SalesCardEvents()

    object NextPage: SalesCardEvents()

    data class UpdateQuery(val query: String): SalesCardEvents()


    object GetOrderAndFilter: SalesCardEvents()

    data class Error(val stateMessage: StateMessage): SalesCardEvents()

    object OnRemoveHeadFromQueue: SalesCardEvents()
}