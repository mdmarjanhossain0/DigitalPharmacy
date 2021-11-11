package com.devscore.digital_pharmacy.presentation.cashregister.receive

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devscore.digital_pharmacy.business.domain.models.*
import com.devscore.digital_pharmacy.business.domain.util.StateMessage
import com.devscore.digital_pharmacy.business.domain.util.UIComponentType
import com.devscore.digital_pharmacy.business.domain.util.doesMessageAlreadyExistInQueue
import com.devscore.digital_pharmacy.business.interactors.cashregister.CreateReceiveInteractor
import com.devscore.digital_pharmacy.business.interactors.supplier.CreateSupplierInteractor
import com.devscore.digital_pharmacy.presentation.session.SessionManager
import com.devscore.digital_pharmacy.presentation.supplier.createsupplier.SupplierCreateEvents
import com.devscore.digital_pharmacy.presentation.supplier.createsupplier.SupplierCreateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ReceiveViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val createReceiveInteractor: CreateReceiveInteractor
) : ViewModel() {

    private val TAG: String = "AppDebug"

    val state: MutableLiveData<ReceiveState> = MutableLiveData(ReceiveState())

    init {
    }

    fun onTriggerEvent(event: ReceiveEvents) {
        when (event) {
            is ReceiveEvents.NewReceiveCreate -> {
                createReceive()
            }

            is ReceiveEvents.CacheState -> {
                cacheState(event.receive)
            }

            is ReceiveEvents.AddCustomer -> {
                addCustomer(event.customer)
            }


            is ReceiveEvents.AddSupplier -> {
                addSupplier(event.supplier)
            }

            is ReceiveEvents.Error -> {
                appendToMessageQueue(event.stateMessage)
            }
            is ReceiveEvents.OnRemoveHeadFromQueue -> {
                removeHeadFromQueue()
            }
        }
    }

    private fun addSupplier(supplier: Supplier) {
        state.value?.let { state ->
            this.state.value = state.copy(supplier = supplier)
        }
    }

    private fun addCustomer(customer: Customer) {
        state.value?.let { state ->
            this.state.value = state.copy(customer = customer)
        }
    }

    private fun cacheState(receive : Receive) {
        state.value?.let { state ->
            this.state.value = state.copy(receive = receive)
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

    private fun createReceive() {
        state.value?.let { state ->
            createReceiveInteractor.execute(
                authToken = sessionManager.state.value?.authToken,
                createReceive = state.receive.toCreateReceive()
            ).onEach { dataState ->
                Log.d(TAG, "ViewModel " + dataState.toString())
                this.state.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { receive ->
                    this.state.value = state.copy(receive = receive)
                }

                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }

            }.launchIn(viewModelScope)
        }
    }

}