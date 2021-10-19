package com.devscore.digital_pharmacy.presentation.inventory.add.addmedicine

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.toAddMedicine
import com.devscore.digital_pharmacy.business.domain.util.ErrorHandling
import com.devscore.digital_pharmacy.business.domain.util.StateMessage
import com.devscore.digital_pharmacy.business.domain.util.UIComponentType
import com.devscore.digital_pharmacy.business.domain.util.doesMessageAlreadyExistInQueue
import com.devscore.digital_pharmacy.business.interactors.inventory.AddMedicineInteractor
import com.devscore.digital_pharmacy.presentation.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddMedicineViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val addMedicineInteractor: AddMedicineInteractor
) : ViewModel() {

    private val TAG: String = "AppDebug"

    val state: MutableLiveData<AddMedicineState> = MutableLiveData(AddMedicineState())

    init {
        onTriggerEvent(AddMedicineEvents.NewAddMedicine)
    }

    fun onTriggerEvent(event: AddMedicineEvents) {
        when (event) {
            is AddMedicineEvents.NewAddMedicine -> {
                addLocalMedicine()
            }

            is AddMedicineEvents.CacheState -> {
                cacheState(event.local_medicine)
            }

            is AddMedicineEvents.Error -> {
                appendToMessageQueue(event.stateMessage)
            }
            is AddMedicineEvents.OnRemoveHeadFromQueue -> {
                removeHeadFromQueue()
            }
        }
    }

    private fun cacheState(local_medicine : LocalMedicine) {
        state.value?.let { state ->
            this.state.value = state.copy(medicine = local_medicine)
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

    private fun addLocalMedicine() {
        state.value?.let { state ->
            addMedicineInteractor.execute(
                authToken = sessionManager.state.value?.authToken,
                medicine = state.medicine.toAddMedicine()
            ).onEach { dataState ->
                Log.d(TAG, "ViewModel " + dataState.toString())
                this.state.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { medicine ->
                    this.state.value = state.copy(medicine = medicine)
                }

                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }

            }.launchIn(viewModelScope)
        }
    }

}