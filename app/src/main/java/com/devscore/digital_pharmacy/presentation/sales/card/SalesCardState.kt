package com.devscore.digital_pharmacy.presentation.sales.card

import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.SalesCart
import com.devscore.digital_pharmacy.business.domain.models.SalesOrder
import com.devscore.digital_pharmacy.business.domain.models.SalesOrderMedicine
import com.devscore.digital_pharmacy.business.domain.util.Queue
import com.devscore.digital_pharmacy.business.domain.util.StateMessage

data class SalesCardState (
    val isLoading : Boolean = false,
    val order : SalesOrder = SalesOrder(
        pk = -2,
        customer = -1,
        total_amount = 0f,
        total_after_discount = .0f,
        paid_amount = 0f,
        discount = 0f,
        is_discount_percent = false,
        created_at = "",
        updated_at = "",
        sales_oder_medicines = ArrayList<SalesOrderMedicine>()
    ),
    val salesCartList : List<SalesCart> = listOf(),
    val totalAmount : Float? = 0f,
    val is_discount_percent : Boolean = false,
    val receivedAmount : Float? = 0f,
    val discount : Float? = 0f,
    val discountAmount : Float? = 0f,
    val totalAmountAfterDiscount : Float? = 0f,
    val medicineList : List<LocalMedicine> = listOf(),
    val query: String = "",
    val page: Int = 1,
    val isQueryExhausted: Boolean = false, // no more results available, prevent next page
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)