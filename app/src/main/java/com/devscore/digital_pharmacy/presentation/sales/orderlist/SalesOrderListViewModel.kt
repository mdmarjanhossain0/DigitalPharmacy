package com.devscore.digital_pharmacy.presentation.sales.orderlist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devscore.digital_pharmacy.business.domain.models.SalesOrder
import com.devscore.digital_pharmacy.business.domain.util.ErrorHandling
import com.devscore.digital_pharmacy.business.domain.util.StateMessage
import com.devscore.digital_pharmacy.business.domain.util.UIComponentType
import com.devscore.digital_pharmacy.business.domain.util.doesMessageAlreadyExistInQueue
import com.devscore.digital_pharmacy.business.interactors.sales.SalesCompleted
import com.devscore.digital_pharmacy.business.interactors.sales.SearchSalesOder
import com.devscore.digital_pharmacy.presentation.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SalesOrderListViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val searchSalesOder: SearchSalesOder,
    private val salesCompleted: SalesCompleted
) : ViewModel() {

    private val TAG: String = "AppDebug"

    val state: MutableLiveData<SalesOrderListState> = MutableLiveData(SalesOrderListState())

    init {
        onTriggerEvent(SalesOrderListEvents.SearchOrders)
    }

    fun onTriggerEvent(event: SalesOrderListEvents) {
        when (event) {
            is SalesOrderListEvents.SearchOrders -> {
                search()
            }

            is SalesOrderListEvents.SearchWithQuery -> {
            }
            is SalesOrderListEvents.NextPage -> {
                incrementPageNumber()
                search()
            }


            is SalesOrderListEvents.SalesCompleted -> {
                orderCompleted(event.order)
            }

            is SalesOrderListEvents.UpdateQuery -> {
                onUpdateQuery(event.query)
            }

            is SalesOrderListEvents.Error -> {
                appendToMessageQueue(event.stateMessage)
            }
            is SalesOrderListEvents.OnRemoveHeadFromQueue -> {
                removeHeadFromQueue()
            }
        }
    }

    private fun orderCompleted(order: SalesOrder) {
        state.value?.let { state ->
            salesCompleted.execute(
                authToken = sessionManager.state.value?.authToken,
                pk = order.pk!!,
                query = state.query,
                status = 0,
                page = state.page,
            ).onEach { dataState ->
                Log.d(TAG, "ViewModel " + dataState.toString())
                this.state.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { list ->
                    Log.d(TAG, "ViewModel List Size " + list.size)
                    this.state.value = state.copy(orderList = list)
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
        state.value?.let { state ->
            this.state.value = state.copy(orderList = listOf())
        }
    }

    private fun resetPage() {
        state.value = state.value?.copy(page = 1)
        onUpdateQueryExhausted(false)
    }

    private fun incrementPageNumber() {
        state.value?.let { state ->
            val pageNumber : Int = (state.orderList.size / 5) as Int + 1
            Log.d(TAG, "Pre increment page number " + pageNumber)
            this.state.value = state.copy(page = pageNumber)
        }
//        state.value?.let { state ->
//            this.state.value = state.copy(page = state.page + 1)
//        }
    }

    private fun onUpdateQuery(query: String) {
        state.value = state.value?.copy(query = query)
    }


    private fun search() {
//        resetPage()
//        clearList()


        Log.d(TAG, "ViewModel page number " + state.value?.page)
        state.value?.let { state ->
            searchSalesOder.execute(
                authToken = sessionManager.state.value?.authToken,
                query = state.query,
                status = 0,
                page = state.page,
            ).onEach { dataState ->
                Log.d(TAG, "ViewModel " + dataState.toString())
                this.state.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { list ->
                    Log.d(TAG, "ViewModel List Size " + list.size)
                    this.state.value = state.copy(orderList = list)
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