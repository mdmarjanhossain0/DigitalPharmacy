package com.devscore.digital_pharmacy.presentation.cashregister.payment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devscore.digital_pharmacy.business.domain.models.Customer
import com.devscore.digital_pharmacy.business.domain.models.Payment
import com.devscore.digital_pharmacy.business.domain.models.Supplier
import com.devscore.digital_pharmacy.business.domain.models.toCreatePayment
import com.devscore.digital_pharmacy.business.domain.util.StateMessage
import com.devscore.digital_pharmacy.business.domain.util.UIComponentType
import com.devscore.digital_pharmacy.business.domain.util.doesMessageAlreadyExistInQueue
import com.devscore.digital_pharmacy.business.interactors.cashregister.CreatePaymentInteractor
import com.devscore.digital_pharmacy.presentation.cashregister.receive.ReceiveEvents
import com.devscore.digital_pharmacy.presentation.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val createPaymentInteractor: CreatePaymentInteractor
) : ViewModel() {

    private val TAG: String = "AppDebug"

    val state: MutableLiveData<PaymentState> = MutableLiveData(PaymentState())

    init {
    }

    fun onTriggerEvent(event: PaymentEvents) {
        when (event) {
            is PaymentEvents.NewPaymentCreate -> {
                createPayment()
            }

            is PaymentEvents.CacheState -> {
                cacheState(event.payment)
            }


            is PaymentEvents.AddCustomer -> {
                addCustomer(event.customer)
            }


            is PaymentEvents.AddSupplier -> {
                addSupplier(event.supplier)
            }

            is PaymentEvents.Error -> {
                appendToMessageQueue(event.stateMessage)
            }
            is PaymentEvents.OnRemoveHeadFromQueue -> {
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


    private fun cacheState(payment : Payment) {
        state.value?.let { state ->
            this.state.value = state.copy(payment = payment)
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

    private fun createPayment() {
        state.value?.let { state ->
            createPaymentInteractor.execute(
                authToken = sessionManager.state.value?.authToken,
                createPayment = state.payment.toCreatePayment()
            ).onEach { dataState ->
                Log.d(TAG, "ViewModel " + dataState.toString())
                this.state.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { payment ->
                    this.state.value = state.copy(payment = payment)
                }

                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }

            }.launchIn(viewModelScope)
        }
    }

}