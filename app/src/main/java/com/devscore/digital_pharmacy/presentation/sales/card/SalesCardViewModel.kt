package com.devscore.digital_pharmacy.presentation.sales.card

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devscore.digital_pharmacy.business.domain.models.*
import com.devscore.digital_pharmacy.business.domain.util.ErrorHandling
import com.devscore.digital_pharmacy.business.domain.util.StateMessage
import com.devscore.digital_pharmacy.business.domain.util.UIComponentType
import com.devscore.digital_pharmacy.business.domain.util.doesMessageAlreadyExistInQueue
import com.devscore.digital_pharmacy.business.interactors.inventory.local.SearchLocalMedicine
import com.devscore.digital_pharmacy.business.interactors.sales.CreateSalesOderInteractor
import com.devscore.digital_pharmacy.presentation.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SalesCardViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val createSalesOrder : CreateSalesOderInteractor,
    private val searchLocalMedicine: SearchLocalMedicine
) : ViewModel() {

    private val TAG: String = "AppDebug"

    val state: MutableLiveData<SalesCardState> = MutableLiveData(SalesCardState())


    lateinit var searchJob : Job

    init {
    }

    fun onTriggerEvent(event: SalesCardEvents) {
        when (event) {
            is SalesCardEvents.GenerateNewOrder -> {
                createNewOrder()
            }

            is SalesCardEvents.NewLocalMedicineSearch -> {
                search()
            }

            is SalesCardEvents.AddToCard -> {
                addToCard(event.medicine)
            }


            is SalesCardEvents.ChangeUnit -> {
                changeUnit(event.medicine, event.unit, event.quantity)
            }
            is SalesCardEvents.NextPage -> {
                incrementPageNumber()
                search()
            }

            is SalesCardEvents.UpdateQuery -> {
                onUpdateQuery(event.query)
            }

            is SalesCardEvents.Error -> {
                appendToMessageQueue(event.stateMessage)
            }
            is SalesCardEvents.OnRemoveHeadFromQueue -> {
                removeHeadFromQueue()
            }
        }
    }

    private fun changeUnit(medicine: LocalMedicine, unitId: Int?, quantity : Int? = 1) {
        state.value?.let { state ->
            var checkExist = 0
            for (item in state.salesCartList) {
                if (item.medicine?.id == medicine.id) {
                    checkExist = 1
                    break
                }
            }
            if (checkExist == 0 ) {
                return@changeUnit
            }
            Log.d(TAG, "Medicine exist in cart")
            Log.d(TAG, "Show Medicine property " + medicine.toString())

            val previousAmount = state.totalAmount
            Log.d(TAG, "Previous Amount " + previousAmount.toString())
            var unitEquivalentQuantity : Int = 0
            var salesUnit : MedicineUnits? = null
            for (unit in medicine.units) {
                if (unit.id == unitId) {
                    unitEquivalentQuantity = unit.quantity
                    salesUnit = unit
                    break
                }
            }
            if (unitEquivalentQuantity == 0) {
                throw Exception("Unit Not Found")
            }
            if (salesUnit == null) {
                throw Exception("Unit cann't usable")
            }

            val newAmount = medicine.mrp!! * quantity!! * unitEquivalentQuantity!!
            Log.d(TAG, "New Amount " + newAmount.toString())

            var previousSalesCartItem : SalesCart? = null

            for (item in state.salesCartList) {
                if (item.medicine?.id == medicine.id) {
                    previousSalesCartItem = item
                }
            }

            if (previousSalesCartItem == null) {
                throw Exception("Item Not Found")
            }


            val totalAmount = previousAmount!! + newAmount - previousSalesCartItem.amount!!

            val newCartList = state.salesCartList.toMutableList()
            for (item in state.salesCartList) {
                if (item.medicine?.id == medicine.id) {
                    newCartList.set(state.salesCartList.indexOf(item), SalesCart(
                        medicine = medicine,
                        salesUnit = salesUnit,
                        quantity = quantity,
                        amount = newAmount
                    ))
                    break
                }
            }

            this.state.value = state.copy(
                salesCartList = newCartList,
                totalAmount = totalAmount
            )
        }
    }

    private fun addToCard(medicine : LocalMedicine, quantity : Int = 1, unitId : Int = 1) {
        state.value?.let { state ->

            for (item in state.salesCartList) {
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

            val newCartList = state.salesCartList.toMutableList()
            newCartList.add(SalesCart(
                medicine = medicine,
                salesUnit = salesUnit!!,
                quantity = quantity,
                amount = newAmount
            ))


            this.state.value = state.copy(
                salesCartList = newCartList,
                totalAmount = totalAmount
            )
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
            createSalesOrder.execute(
                authToken = sessionManager.state.value?.authToken,
                state.order.toCreateSalesOrder()
            ).onEach { dataState ->
                Log.d(TAG, "ViewModel " + dataState.toString())
                this.state.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { order ->
                    this.state.value = state.copy(order = order)
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
                order = SalesOrder(
                    pk = -2,
                    customer = -1,
                    total_amount = state.totalAmount?.toFloat(),
                    total_after_discount = .0f,
                    paid_amount = 0f,
                    discount = 0f,
                    is_discount_percent = false,
                    created_at = "",
                    updated_at = "",
                    sales_oder_medicines = list
                )
            )
        }
    }

    private fun processOrderMedicine() : List<SalesOrderMedicine> {
        val list = mutableListOf<SalesOrderMedicine>()
        state.value?.let {state ->
            for (item in state.salesCartList) {
                list.add(
                    SalesOrderMedicine(
                        unit = item.salesUnit?.id!!,
                        quantity = item.quantity?.toFloat()!!,
                        local_medicine = item.medicine?.id!!,
                        brand_name = item.medicine?.brand_name!!
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