package com.devscore.digital_pharmacy.presentation.sales.card

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.SalesOrderMedicine
import com.devscore.digital_pharmacy.business.domain.models.toCreateSalesOrder
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

    private fun addToCard(medicine : LocalMedicine) {
        state.value?.let { state ->
            for (item in state.order.sales_oder_medicines!!) {
                if (item.local_medicine == medicine.id) {
                    return@addToCard
                }
            }
            state.order.sales_oder_medicines?.toMutableList()?.add(
                SalesOrderMedicine(
                    unit = -1,
                    quantity = 1f,
                    local_medicine = medicine.id!!,
                    brand_name = medicine.brand_name
                )
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
        resetPage()
        clearList()


        Log.d(TAG, "ViewModel page number " + state.value?.page)
        if (searchJob != null) {
            if (searchJob.isActive) {
                searchJob.cancel()
            }
        }
        state.value?.let { state ->
            searchJob = createSalesOrder.execute(
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