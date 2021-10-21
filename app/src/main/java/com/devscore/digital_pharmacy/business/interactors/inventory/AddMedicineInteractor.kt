package com.devscore.digital_pharmacy.business.interactors.inventory

import android.content.Context
import android.util.Log
import androidx.work.*
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.*
import com.devscore.digital_pharmacy.business.datasource.network.handleUseCaseException
import com.devscore.digital_pharmacy.business.datasource.network.inventory.InventoryApiService
import com.devscore.digital_pharmacy.business.datasource.network.inventory.network_responses.toLocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.AddMedicine
import com.devscore.digital_pharmacy.business.domain.models.AuthToken
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.toLocalMedicine
import com.devscore.digital_pharmacy.business.domain.util.*
import com.devscore.digital_pharmacy.presentation.util.SyncWorker
import com.devscore.digital_pharmacy.presentation.util.UploadWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class AddMedicineInteractor(
    private val service: InventoryApiService,
    private val cache: LocalMedicineDao,
    private val context: Context
) {

    private val TAG: String = "AppDebug"

    fun execute(
        authToken: AuthToken?,
        medicine: AddMedicine
    ): Flow<DataState<LocalMedicine>> = flow {

        emit(DataState.loading<LocalMedicine>())
        if(authToken == null){
            throw Exception(ErrorHandling.ERROR_AUTH_TOKEN_INVALID)
        }

        var message  = " "

        try{ // catch network exception
            Log.d(TAG, "Call Api Section")
            val medicineResponse = service.addMedicine(
                "Token ${authToken.token}",
                medicine = medicine
            ).toLocalMedicine()


            try{
                cache.insertLocalMedicine(medicineResponse.toLocalMedicineEntity())
                for (unit in medicineResponse.toLocalMedicineUnitEntity()) {
                    cache.insertLocalMedicineUnit(unit)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }

        }catch (e: Exception){
            Log.d(TAG, "Exception " + e.toString())

            try{
                cache.insertFailureMedicine(medicine.toLocalMedicine().toFailureMedicine())
                for (unit in medicine.toLocalMedicine().toFailureMedicineUnitEntity()) {
                    cache.insertFailureMedicineUnit(unit)
                }
            }catch (e: Exception){
                Log.d(TAG, "Room Exception Enter Exception is Exception " + e.toString())
                e.printStackTrace()

                val constraints = Constraints.Builder()
                    .setRequiresCharging(false)
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                val syncWorkRequest : WorkRequest =
                    OneTimeWorkRequestBuilder<UploadWorker>()
                        .setConstraints(constraints)
                        .build()

                WorkManager
                    .getInstance(context)
                    .enqueue(syncWorkRequest)
            }


            emit(
                DataState.error<LocalMedicine>(
                    response = Response(
                        message = "Unable to upload medicine. Please careful and don't uninstall or log out",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    )
                )
            )
            return@flow
        }


        val stateMedicine = medicine.toLocalMedicine()

        emit(DataState.data(response = Response(
            message = "Successfully Uploaded.",
            uiComponentType = UIComponentType.Dialog(),
            messageType = MessageType.Success()
        ), data = stateMedicine))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}