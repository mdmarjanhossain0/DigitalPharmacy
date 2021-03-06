package com.devscore.digital_pharmacy.business.interactors.purchases

import com.devscore.digital_pharmacy.business.datasource.cache.purchases.PurchasesDao
import com.devscore.digital_pharmacy.business.datasource.cache.purchases.toPurchasesOder
import com.devscore.digital_pharmacy.business.datasource.cache.sales.SalesDao
import com.devscore.digital_pharmacy.business.datasource.cache.sales.toSalesOder
import com.devscore.digital_pharmacy.business.datasource.network.handleUseCaseException
import com.devscore.digital_pharmacy.business.datasource.network.purchases.PurchasesApiService
import com.devscore.digital_pharmacy.business.datasource.network.sales.SalesApiService
import com.devscore.digital_pharmacy.business.domain.models.AuthToken
import com.devscore.digital_pharmacy.business.domain.models.PurchasesOrder
import com.devscore.digital_pharmacy.business.domain.models.SalesOrder
import com.devscore.digital_pharmacy.business.domain.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class PurchasesOrderDetailsInteractor (
    private val service : PurchasesApiService,
    private val cache : PurchasesDao
) {

    private val TAG: String = "AppDebug"

    fun execute(
        authToken: AuthToken?,
        pk : Int
    ): Flow<DataState<PurchasesOrder>> = flow {
        emit(DataState.loading<PurchasesOrder>())
        if(authToken == null){
            throw Exception(ErrorHandling.ERROR_AUTH_TOKEN_INVALID)
        }

        try {
            val order = cache.getPurchasesOrder(pk).toPurchasesOder()
            emit(DataState.data(response = null, data = order))
            return@flow
        }
        catch (e : java.lang.Exception) {
            e.printStackTrace()
//            val order = cache.getFailureSalesOrder(pk).toSalesOder()
//            if (order != null) {
//                emit(DataState.data(response = null, data = order))
//                return@flow
//            }
        }

        emit(
            DataState.error<PurchasesOrder>(
                response = Response(
                    message = "Order not found",
                    uiComponentType = UIComponentType.Dialog(),
                    messageType = MessageType.Error()
                )
            )
        )

    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}