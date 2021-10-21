package com.devscore.digital_pharmacy.presentation.inventory.global

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devscore.digital_pharmacy.business.interactors.inventory.SearchGlobalMedicine
import com.devscore.digital_pharmacy.business.domain.util.ErrorHandling
import com.devscore.digital_pharmacy.business.domain.util.StateMessage
import com.devscore.digital_pharmacy.business.domain.util.UIComponentType
import com.devscore.digital_pharmacy.business.domain.util.doesMessageAlreadyExistInQueue
import com.devscore.digital_pharmacy.presentation.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class GlobalViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val searchGlobalMedicine: SearchGlobalMedicine
) : ViewModel() {

    private val TAG: String = "AppDebug"

    val state: MutableLiveData<GlobalState> = MutableLiveData(GlobalState())

    init {
        onTriggerEvent(GlobalEvents.NewMedicineSearch)
    }

    fun onTriggerEvent(event: GlobalEvents) {
        when (event) {
            is GlobalEvents.NewMedicineSearch -> {
                search()
            }
            is GlobalEvents.NextPage -> {
            }

            is GlobalEvents.Error -> {
                appendToMessageQueue(event.stateMessage)
            }
            is GlobalEvents.OnRemoveHeadFromQueue -> {
                removeHeadFromQueue()
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
        state.value?.let { state ->
            this.state.value = state.copy(globalMedicineList = listOf())
        }
    }

    private fun resetPage() {
        state.value = state.value?.copy(page = 1)
        onUpdateQueryExhausted(false)
    }

    private fun incrementPageNumber() {
        state.value?.let { state ->
            this.state.value = state.copy(page = state.page + 1)
        }
    }

    private fun onUpdateQuery(query: String) {
        state.value = state.value?.copy(query = query)
    }


    private fun search() {
//        resetPage()
//        clearList()
        state.value?.let { state ->
            searchGlobalMedicine.execute(
                authToken = sessionManager.state.value?.authToken,
                query = state.query,
                page = state.page,
            ).onEach { dataState ->
                Log.d(TAG, "ViewModel " + dataState.toString())
                this.state.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { list ->
                    Log.d(TAG, "ViewModel List Size " + list.size)
                    this.state.value = state.copy(globalMedicineList = list)
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