package com.devscore.digital_pharmacy.business.interactors.inventory

import com.devscore.digital_pharmacy.business.datasource.cache.inventory.GlobalMedicineDao
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.toGlobalMedicine
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.toGlobalMedicineEntity
import com.devscore.digital_pharmacy.business.datasource.network.handleUseCaseException
import com.devscore.digital_pharmacy.business.datasource.network.inventory.InventoryApiService
import com.devscore.digital_pharmacy.business.datasource.network.inventory.toGlobalMedicine
import com.devscore.digital_pharmacy.business.domain.models.AuthToken
import com.devscore.digital_pharmacy.business.domain.models.GlobalMedicine
import com.devscore.digital_pharmacy.business.domain.util.DataState
import com.devscore.digital_pharmacy.business.domain.util.ErrorHandling.Companion.ERROR_AUTH_TOKEN_INVALID
import com.devscore.digital_pharmacy.business.domain.util.MessageType
import com.devscore.digital_pharmacy.business.domain.util.Response
import com.devscore.digital_pharmacy.business.domain.util.UIComponentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class SearchGlobalMedicine(
    private val service: InventoryApiService,
    private val cache: GlobalMedicineDao,
) {

    private val TAG: String = "AppDebug"

    fun execute(
        authToken: AuthToken?,
        query: String,
        page: Int
    ): Flow<DataState<List<GlobalMedicine>>> = flow {
        emit(DataState.loading<List<GlobalMedicine>>())
        if(authToken == null){
            throw Exception(ERROR_AUTH_TOKEN_INVALID)
        }

        try{ // catch network exception
            val medicines = service.searchAllListGlobalMedicine(
                "Token ${authToken.token}",
                query = query,
                page = page
            ).results?.map { it.toGlobalMedicine() }

            // Insert into cache
            for(medicine in medicines!!){
                try{
                    cache.insert(medicine.toGlobalMedicineEntity())
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }catch (e: Exception){
            emit(
                DataState.error<List<GlobalMedicine>>(
                    response = Response(
                        message = "Unable to update the cache.",
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Error()
                    )
                )
            )
        }

        // emit from cache
        val cachedBlogs = cache.getAllGlobalMedicine(
            page = page
        ).map { it.toGlobalMedicine() }

        emit(DataState.data(response = null, data = cachedBlogs))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}



















