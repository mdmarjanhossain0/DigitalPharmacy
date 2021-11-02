package com.devscore.digital_pharmacy.business.interactors.purchases

import android.util.Log
import com.devscore.digital_pharmacy.business.datasource.cache.purchases.PurchasesDao
import com.devscore.digital_pharmacy.business.datasource.cache.purchases.toPurchasesOder
import com.devscore.digital_pharmacy.business.datasource.cache.purchases.toPurchasesOrder
import com.devscore.digital_pharmacy.business.datasource.network.handleUseCaseException
import com.devscore.digital_pharmacy.business.datasource.network.purchases.PurchasesApiService
import com.devscore.digital_pharmacy.business.datasource.network.purchases.toPurchasesOder
import com.devscore.digital_pharmacy.business.domain.models.AuthToken
import com.devscore.digital_pharmacy.business.domain.models.PurchasesOrder
import com.devscore.digital_pharmacy.business.domain.models.toPurchasesOrderEntity
import com.devscore.digital_pharmacy.business.domain.models.toPurchasesOrderMedicines
import com.devscore.digital_pharmacy.business.domain.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class PurchasesCompleted (
    private val service : PurchasesApiService,
    private val cache : PurchasesDao
) {

    private val TAG: String = "AppDebug"

    fun execute(
        authToken: AuthToken?,
        pk : Int,
        query: String,
        status : Int,
        page: Int
    ): Flow<DataState<List<PurchasesOrder>>> = flow {
        emit(DataState.loading<List<PurchasesOrder>>())
        if(authToken == null){
            throw Exception(ErrorHandling.ERROR_AUTH_TOKEN_INVALID)
        }

        try{
            Log.d(TAG, "Call Api Section")
            val result = service.purchasesCompleted(
                "Token ${authToken.token}",
                pk
            )

            Log.d(TAG, result.toString())

            val order = result.toPurchasesOder()

            try{
                Log.d(TAG, "Data " + order.toString())
                cache.insertPurchasesOrder(order.toPurchasesOrderEntity())
                for (medicine in order.toPurchasesOrderMedicines()) {
                    Log.d(TAG, "Order Medicine " + medicine.toString())
                    cache.insertPurchasesOrderMedicine(medicine)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }

        }catch (e: Exception){
            e.printStackTrace()
            emit(
                DataState.error<List<PurchasesOrder>>(
                    response = Response(
                        message = "Unable to update the cache.",
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Error()
                    )
                )
            )
        }
        Log.d(TAG, "Purchases Search Status " + status.toString())

        val successList = cache.searchPurchasesOrderWithMedicine(
            query = query,
            status = status,
            page = page
        ).map {
            Log.d(TAG, "Purchases Search SuccessList " + it.toString())
            it.toPurchasesOder()
        }

        val failureList = cache.searchFailurePurchasesOrderWithMedicine(
            query = query,
            status = status
        ).map {
            it.toPurchasesOrder()
        }
        Log.d(TAG, "Purchases Search FailureList " + failureList.toString())





        val  list = marge(successList, failureList)
        emit(DataState.data(response = null, data = list))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}
