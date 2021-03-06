package com.devscore.digital_pharmacy.presentation.purchases.cart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devscore.digital_pharmacy.business.domain.models.*
import com.devscore.digital_pharmacy.business.domain.util.*
import com.devscore.digital_pharmacy.business.interactors.inventory.local.SearchLocalMedicine
import com.devscore.digital_pharmacy.business.interactors.purchases.CreatePurchasesOrderInteractor
import com.devscore.digital_pharmacy.presentation.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PurchasesCartViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val createPurchasesOrderInteractor: CreatePurchasesOrderInteractor,
    private val searchLocalMedicine: SearchLocalMedicine
) : ViewModel() {

    private val TAG: String = "AppDebug"

    val state: MutableLiveData<PurchasesCartState> = MutableLiveData(PurchasesCartState())



    init {
    }

    fun onTriggerEvent(event: PurchasesCartEvents) {
        when (event) {
            is PurchasesCartEvents.GenerateNewOrder -> {
                createNewOrder()
            }

            is PurchasesCartEvents.NewLocalMedicineSearch -> {
                search()
            }

            is PurchasesCartEvents.AddToCard -> {
                addToCard(event.medicine)
            }


            is PurchasesCartEvents.ChangeUnit -> {
                changeUnit(event.cart, event.unit!!, event.quantity!!)
            }

            is PurchasesCartEvents.ReceiveAmount -> {
                receiveAmount(event.amount!!)
            }

            is PurchasesCartEvents.IsDiscountPercent -> {
                isDiscountPercent(event.isDiscountPercent)
            }

            is PurchasesCartEvents.Discount -> {
                discount(event.discount)
            }

            is PurchasesCartEvents.DeleteMedicine -> {
                deleteFromCart(event.medicine)
            }

            is PurchasesCartEvents.SelectSupplier -> {
                selectSupplier(event.supplier)
            }
            is PurchasesCartEvents.NextPage -> {
                incrementPageNumber()
                search()
            }

            is PurchasesCartEvents.UpdateQuery -> {
                onUpdateQuery(event.query)
            }

            is PurchasesCartEvents.Error -> {
                appendToMessageQueue(event.stateMessage)
            }
            is PurchasesCartEvents.OnRemoveHeadFromQueue -> {
                removeHeadFromQueue()
            }
        }
    }

    private fun selectSupplier(supplier: Supplier) {
        state.value?.let { state ->
            this.state.value = state.copy(
                vendor = supplier
            )
        }
    }

    private fun deleteFromCart(medicine : LocalMedicine) {
        state.value?.let { state ->
            var checkExist = 0
            for (item in state.purchasesCartList) {
                if (item.medicine?.id == medicine.id) {
                    checkExist = 1
                    break
                }
            }
            if (checkExist == 0 ) {
                return@deleteFromCart
            }
            Log.d(TAG, "Medicine exist in cart")
            Log.d(TAG, "Show Medicine property " + medicine.toString())

            val previousAmount = state.totalAmount
            Log.d(TAG, "Previous Amount " + previousAmount.toString())

            var previousSalesCartItem : PurchasesCart? = null

            for (item in state.purchasesCartList) {
                if (item.medicine?.id == medicine.id) {
                    previousSalesCartItem = item
                }
            }

            if (previousSalesCartItem == null) {
                throw Exception("Item Not Found")
            }


            val totalAmount = previousAmount!! - previousSalesCartItem.amount!!

            val newCartList = state.purchasesCartList.toMutableList()
            for (item in state.purchasesCartList) {
                if (item.medicine?.id == medicine.id) {
                    newCartList.remove(item)
                    break
                }
            }

            this.state.value = state.copy(
                purchasesCartList = newCartList,
                totalAmount = totalAmount,
                totalAmountAfterDiscount = totalAmount,
            )
        }
    }

    private fun discount(discount: Float?) {
        state.value?.let {state ->
            var discountAmount = 0f
            if (state.is_discount_percent) {
                discountAmount = ((state.totalAmount!! * discount!!) / 100)
            }
            else {
                discountAmount = discount!!
            }
            val totalAmountAfterDiscount = state.totalAmount!! - discountAmount
            this.state.value = state.copy(
                discount = discount,
                discountAmount = discountAmount,
                totalAmountAfterDiscount = totalAmountAfterDiscount
            )
        }
    }

    private fun isDiscountPercent(discountPercent: Boolean) {
        state.value?.let {state ->
            var discountAmount : Float = 0f
            if (discountPercent) {
                discountAmount = ((state.totalAmount!! * state.discount!!) / 100 )
            }
            else {
                discountAmount = state.discount!!
            }
            val totalAmountAfterDiscount = state.totalAmount!! - discountAmount
            this.state.value = state.copy(
                is_discount_percent = discountPercent,
                discountAmount = discountAmount,
                totalAmountAfterDiscount = totalAmountAfterDiscount
            )
        }
    }

    private fun receiveAmount(amount : Float) {
        state.value?.let {state ->
            this.state.value = state.copy(
                receivedAmount = amount
            )
        }
    }

    private fun changeUnit(cart : PurchasesCart, unit: MedicineUnits, quantity : Int) {
        state.value?.let { state ->
            val previousAmount = state.totalAmount
            Log.d(TAG, "Previous Amount " + previousAmount.toString())
            val unitEquivalentQuantity = unit.quantity

            val newAmount = cart.medicine?.purchases_price!! * quantity!! * unitEquivalentQuantity
            Log.d(TAG, "New Amount " + newAmount.toString())

            val totalAmount = previousAmount!! + newAmount - cart.amount!!

            val previousCartList = state.purchasesCartList.toMutableList()
//            val newCartList = mutableListOf<SalesCart>()
            previousCartList.find {
                it.medicine?.id == cart.medicine?.id!!
            }?.purchasesUnit = unit

            previousCartList.find {
                it.medicine?.id == cart.medicine?.id!!
            }?.amount = newAmount


            previousCartList.find {
                it.medicine?.id == cart.medicine?.id!!
            }?.quantity = quantity


            Log.d(TAG, "Previous Cart List " + previousCartList.size + " " + previousCartList.toString())

            this.state.value = state.copy(
                purchasesCartList = previousCartList,
                totalAmount = totalAmount,
                totalAmountAfterDiscount = totalAmount
            )
        }
    }

    private fun addToCard(medicine : LocalMedicine, quantity : Int = 1, unitId : Int = 1) {
        state.value?.let { state ->

            try {
                for (item in state.purchasesCartList) {
                    if (item.medicine?.id == medicine.id) {
                        return@addToCard
                    }
                }
                Log.d(TAG, "Successfully Add To Cart")
                Log.d(TAG, "Show Medicine property " + medicine.toString())

                val previousAmount = state.totalAmount?.toInt()
                Log.d(TAG, "Previous Amount " + previousAmount.toString())
                var unitEquivalentQuantity : Int = 0
                var salesUnit : MedicineUnits? = null
                for (unit in medicine.units) {
                    if (unit.type == "SALES") {
                        unitEquivalentQuantity = unit.quantity
                        salesUnit = unit
                        break
                    }
                }
                if (unitEquivalentQuantity == 0) {
                    unitEquivalentQuantity = medicine.units.first().quantity
                    salesUnit = medicine.units.first()
                }
                if (unitEquivalentQuantity == 0) {
                    throw Exception("Unit Not Found")
                }

                if (salesUnit == null) {
                    throw Exception("Unit Not Found")
                }
                val newAmount = medicine.mrp!! * quantity!! * unitEquivalentQuantity!!
                Log.d(TAG, "New Amount " + newAmount.toString())

                val totalAmount = newAmount!! + previousAmount!!

                val newCartList = state.purchasesCartList.toMutableList()
                newCartList.add(
                    PurchasesCart(
                        medicine = medicine,
                        purchasesUnit = salesUnit!!,
                        quantity = quantity,
                        amount = newAmount
                    )
                )


                this.state.value = state.copy(
                    purchasesCartList = newCartList,
                    totalAmount = totalAmount,
                    totalAmountAfterDiscount = totalAmount
                )
            }
            catch (e : Exception) {
                e.printStackTrace()
                appendToMessageQueue(
                    StateMessage(
                        response = Response(
                            message = medicine.brand_name + " " + " has no unit",
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Error()
                        )
                    )
                )
            }
        }
    }


    private fun removeHeadFromQueue() {
        state.value?.let { state ->
            try {
                val queue = state.queue
                queue.remove() // can throw exception if empty
                this.state.value = state.copy(queue = queue)
            } catch (e: Exception) {
                Log.d(TAG, "removeHeadFromQueue: Nothing to remove from DialogQueue")
            }
        }
    }

    private fun appendToMessageQueue(stateMessage: StateMessage){
        state.value?.let { state ->
            val queue = state.queue
            if(!stateMessage.doesMessageAlreadyExistInQueue(queue = queue)){
                if(!(stateMessage.response.uiComponentType is UIComponentType.None)){
                    queue.add(stateMessage)
                    this.state.value = state.copy(queue = queue)
                }
            }
        }
    }

    private fun onUpdateQueryExhausted(isExhausted: Boolean) {
        state.value?.let { state ->
            this.state.value = state.copy(isQueryExhausted = isExhausted)
        }
    }

    private fun clearList() {
    }

    private fun resetPage() {
        state.value = state.value?.copy(page = 1)
        onUpdateQueryExhausted(false)
    }

    private fun incrementPageNumber() {
        state.value?.let { state ->
            val pageNumber : Int = (state.medicineList.size / 5) as Int + 1
            Log.d(TAG, "Pre increment page number " + pageNumber)
            this.state.value = state.copy(page = pageNumber)
        }
    }

    private fun onUpdateQuery(query: String) {
        state.value = state.value?.copy(query = query)
    }


    private fun createNewOrder() {
        processOder()
        Log.d(TAG, "ViewModel page number " + state.value?.page)
        state.value?.let { state ->
            createPurchasesOrderInteractor.execute(
                authToken = sessionManager.state.value?.authToken,
                state.order.toCreatePurchasesOrder()
            ).onEach { dataState ->
                Log.d(TAG, "ViewModel " + dataState.toString())
                this.state.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { order ->
                    this.state.value = state.copy(
                        order = order,
                        uploaded = true
                    )
                }

                dataState.stateMessage?.let { stateMessage ->
                    if(stateMessage.response.message?.contains(ErrorHandling.INVALID_PAGE) == true){
                        onUpdateQueryExhausted(true)
                    }else{
                        appendToMessageQueue(stateMessage)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    private fun processOder() {
        val list = processOrderMedicine()
        state.value?.let { state ->
            this.state.value = state.copy(
                order = PurchasesOrder(
                    pk = -2,
                    vendor = state.vendor?.pk,
                    company = state.vendor?.company_name,
                    total_amount = state.totalAmount?.toFloat(),
                    total_after_discount = state.totalAmountAfterDiscount?.toFloat(),
                    paid_amount = state.receivedAmount,
                    discount = state.discountAmount,
                    is_discount_percent = (state.discount == state.totalAmountAfterDiscount),
                    status = 0,
                    created_at = "",
                    updated_at = "",
                    purchases_order_medicines = list
                )
            )
        }
    }

    private fun processOrderMedicine() : List<PurchasesOrderMedicine> {
        val list = mutableListOf<PurchasesOrderMedicine>()
        state.value?.let {state ->
            for (item in state.purchasesCartList) {
                list.add(
                    PurchasesOrderMedicine(
                        unit = item.purchasesUnit?.id!!,
                        quantity = item.quantity?.toFloat()!!,
                        local_medicine = item.medicine?.id!!,
                        brand_name = item.medicine?.brand_name!!,
                        unit_name = item.purchasesUnit?.name!!,
                        amount = item.amount
                    )
                )
            }
        }
        return list
    }


    private fun search() {
        resetPage()
//        clearList()


        Log.d(TAG, "ViewModel page number " + state.value?.page)
        state.value?.let { state ->
            searchLocalMedicine.execute(
                authToken = sessionManager.state.value?.authToken,
                query = state.query,
                page = state.page,
            ).onEach { dataState ->
                Log.d(TAG, "ViewModel " + dataState.toString())
                this.state.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { list ->
                    Log.d(TAG, "ViewModel List Size " + list.size)
                    this.state.value = state.copy(medicineList = list)
                }

                dataState.stateMessage?.let { stateMessage ->
                    if(stateMessage.response.message?.contains(ErrorHandling.INVALID_PAGE) == true){
                        onUpdateQueryExhausted(true)
                    }else{
                        appendToMessageQueue(stateMessage)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

}