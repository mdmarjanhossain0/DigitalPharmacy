package com.devscore.digital_pharmacy.business.interactors.inventory.local

import android.util.Log
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.LocalMedicineDao
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.toLocalMedicine
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.toLocalMedicineEntity
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.toLocalMedicineUnitEntity

import com.devscore.digital_pharmacy.business.datasource.network.handleUseCaseException
import com.devscore.digital_pharmacy.business.datasource.network.inventory.InventoryApiService
import com.devscore.digital_pharmacy.business.datasource.network.inventory.toLocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.AuthToken
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class SearchLocalMedicine(
    private val service: InventoryApiService,
    private val cache: LocalMedicineDao,
) {

    private val TAG: String = "AppDebug"

    fun execute(
        authToken: AuthToken?,
        query: String,
        page: Int
    ): Flow<DataState<List<LocalMedicine>>> = flow {
        emit(DataState.loading<List<LocalMedicine>>())
        if(authToken == null){
            throw Exception(ErrorHandling.ERROR_AUTH_TOKEN_INVALID)
        }

        try{
            Log.d(TAG, "Call Api Section")
            val medicines = service.searchLocalMedicineList(
                "Token ${authToken.token}",
                query = query,
                page = page
            ).results.map {
                Log.d(TAG, "looping toLocalMedicine")
                it.toLocalMedicine()
            }

            for(medicine in medicines){
                try{
                    Log.d(TAG, "Caching size" + medicines.size.toString())
                    cache.insertLocalMedicine(medicine.toLocalMedicineEntity())
                    for (unit in medicine.toLocalMedicineUnitEntity()) {
                        cache.insertLocalMedicineUnit(unit)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
            emit(
                DataState.error<List<LocalMedicine>>(
                    response = Response(
                        message = "Unable to update the cache for network exception.",
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Error()
                    )
                )
            )
        }

        val localMedicine = cache.searchLocalMedicineWithUnitWithQuery(
            query = query,
            page = page
        ).map { it.toLocalMedicine() }

        val failureMedicine = cache.searchFailureMedicineWithUnits(
            query = query,
        ).map {
            it.toLocalMedicine()
        }





        emit(DataState.data(response = null, data = marge(localMedicine, failureMedicine)))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}

fun marge(localMedicine: List<LocalMedicine>, failureMedicine : List<LocalMedicine>) : List<LocalMedicine> {
    val allMedicine  = mutableListOf<LocalMedicine>()
    allMedicine.addAll(localMedicine)
    allMedicine.addAll(failureMedicine)
    return allMedicine
}
