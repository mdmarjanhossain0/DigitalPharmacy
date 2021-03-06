package com.devscore.digital_pharmacy.presentation.sales.card

import com.devscore.digital_pharmacy.business.domain.models.Customer
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.MedicineUnits
import com.devscore.digital_pharmacy.business.domain.models.SalesCart
import com.devscore.digital_pharmacy.business.domain.util.StateMessage

sealed class SalesCardEvents {

    object GenerateNewOrder : SalesCardEvents()

    object NewLocalMedicineSearch : SalesCardEvents()

    data class AddToCard(val medicine : LocalMedicine): SalesCardEvents()

    data class UpdateQuantity(val cart : SalesCart, val quantity : Int?) : SalesCardEvents()

    data class ChangeUnit(val cart : SalesCart, val unit : MedicineUnits?, val quantity : Int?) : SalesCardEvents()
//    data class ChangeUnit(val cart : SalesCart, val unit : Int?, val quantity : Int?) : SalesCardEvents()
//    data class ChangeUnit(val medicine: LocalMedicine, val unit : Int?, val quantity : Int?) : SalesCardEvents()

    data class IsDiscountPercent(val isDiscountPercent : Boolean = false) : SalesCardEvents()

    data class ReceiveAmount(val amount : Float? = 0f) : SalesCardEvents()

    data class Discount(val discount : Float? = 0f) : SalesCardEvents()

    data class DeleteMedicine(val medicine : LocalMedicine) : SalesCardEvents()


    data class SelectCustomer(val customer : Customer) : SalesCardEvents()

    data class SearchWithQuery(val query: String) : SalesCardEvents()

    object NextPage: SalesCardEvents()

    data class UpdateQuery(val query: String): SalesCardEvents()


    object GetOrderAndFilter: SalesCardEvents()

    data class Error(val stateMessage: StateMessage): SalesCardEvents()

    object OnRemoveHeadFromQueue: SalesCardEvents()
}