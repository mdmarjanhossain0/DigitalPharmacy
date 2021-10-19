package com.devscore.digital_pharmacy.business.interactors.inventory

import android.util.Log
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.LocalMedicineDao
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.toLocalMedicine
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.toLocalMedicineEntity
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.toLocalMedicineUnitEntity
import com.devscore.digital_pharmacy.business.datasource.network.handleUseCaseException
import com.devscore.digital_pharmacy.business.datasource.network.inventory.InventoryApiService
import com.devscore.digital_pharmacy.business.datasource.network.inventory.network_responses.toLocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.AddMedicine
import com.devscore.digital_pharmacy.business.domain.models.AuthToken
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.toLocalMedicine
import com.devscore.digital_pharmacy.business.domain.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class AddMedicineInteractor(
    private val service: InventoryApiService,
    private val cache: LocalMedicineDao,
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

        var room_medicine_id : Long = -1

        try{ // catch network exception
            Log.d(TAG, "Call Api Section")
            val medicineResponse = service.addMedicine(
                "Token ${authToken.token}",
                medicine = medicine
            ).toLocalMedicine()




            try{
                room_medicine_id = cache.insertLocalMedicine(medicineResponse.toLocalMedicineEntity())
                for (unit in medicineResponse.toLocalMedicineUnitEntity()) {
                    cache.insertLocalMedicineUnit(unit)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }

        }catch (e: Exception){
            Log.d(TAG, "Exception " + e.toString())

            try{
                room_medicine_id = cache.insertLocalMedicine(medicine.toLocalMedicine().toLocalMedicineEntity())
                for (unit in medicine.toLocalMedicine().toLocalMedicineUnitEntity()) {
                    cache.insertLocalMedicineUnit(unit)
                }
            }catch (e: Exception){
                Log.d(TAG, "Room Exception Enter Exception is Exception " + e.toString())
                e.printStackTrace()
            }

            emit(
                DataState.error<LocalMedicine>(
                    response = Response(
                        message = "Unable to update the cache.",
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Error()
                    )
                )
            )
        }


        val cacheLocalMedicines = cache.getLocalMedicineWithUnits(
            room_medicine_id = room_medicine_id
        )?.toLocalMedicine()

        emit(DataState.data(response = null, data = cacheLocalMedicines))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}