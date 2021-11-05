package com.devscore.digital_pharmacy.presentation.purchases.cart

import com.devscore.digital_pharmacy.business.domain.models.*
import com.devscore.digital_pharmacy.business.domain.util.StateMessage
import com.devscore.digital_pharmacy.presentation.sales.card.SalesCardEvents

sealed class PurchasesCartEvents {

    object GenerateNewOrder : PurchasesCartEvents()

    object NewLocalMedicineSearch : PurchasesCartEvents()

    data class AddToCard(val medicine : LocalMedicine): PurchasesCartEvents()

    data class ChangeUnit(val cart : PurchasesCart, val unit : MedicineUnits?, val quantity : Int?) : PurchasesCartEvents()
//    data class ChangeUnit(val medicine: LocalMedicine, val unit : Int?, val quantity : Int?) : PurchasesCartEvents()

    data class IsDiscountPercent(val isDiscountPercent : Boolean = false) : PurchasesCartEvents()

    data class ReceiveAmount(val amount : Float? = 0f) : PurchasesCartEvents()

    data class Discount(val discount : Float? = 0f) : PurchasesCartEvents()

    data class DeleteMedicine(val medicine : LocalMedicine) : PurchasesCartEvents()

    data class SelectSupplier(val supplier : Supplier) : PurchasesCartEvents()

    data class SearchWithQuery(val query: String) : PurchasesCartEvents()

    object NextPage: PurchasesCartEvents()

    data class UpdateQuery(val query: String): PurchasesCartEvents()


    object GetOrderAndFilter: PurchasesCartEvents()

    data class Error(val stateMessage: StateMessage): PurchasesCartEvents()

    object OnRemoveHeadFromQueue: PurchasesCartEvents()
}