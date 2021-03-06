package com.devscore.digital_pharmacy.business.interactors.sales

import android.util.Log
import com.devscore.digital_pharmacy.business.datasource.cache.sales.SalesDao
import com.devscore.digital_pharmacy.business.datasource.cache.sales.toSalesOder
import com.devscore.digital_pharmacy.business.datasource.network.handleUseCaseException
import com.devscore.digital_pharmacy.business.datasource.network.sales.SalesApiService
import com.devscore.digital_pharmacy.business.datasource.network.sales.toSalesOrder
import com.devscore.digital_pharmacy.business.domain.models.AuthToken
import com.devscore.digital_pharmacy.business.domain.models.SalesOrder
import com.devscore.digital_pharmacy.business.domain.models.toSalesOrderEntity
import com.devscore.digital_pharmacy.business.domain.models.toSalesOrderMedicinesEntity
import com.devscore.digital_pharmacy.business.domain.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class SalesCompleted(
    private val service : SalesApiService,
    private val cache : SalesDao
) {

    private val TAG: String = "AppDebug"

    fun execute(
        authToken: AuthToken?,
        pk : Int,
        query: String,
        status : Int,
        page: Int
    ): Flow<DataState<List<SalesOrder>>> = flow {
        emit(DataState.loading<List<SalesOrder>>())
        if(authToken == null){
            throw Exception(ErrorHandling.ERROR_AUTH_TOKEN_INVALID)
        }

        try{
            Log.d(TAG, "Call Api Section")
            val result = service.salesCompleted(
                "Token ${authToken.token}",
                pk
            )

            Log.d(TAG, result.toString())

            val order = result.toSalesOrder()


            try{
                Log.d(TAG, "Data " + order.toString())
                cache.insertSalesOder(order.toSalesOrderEntity())
                for (medicine in order.toSalesOrderMedicinesEntity()) {
                    cache.insertSaleOderMedicine(medicine)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }

        }catch (e: Exception){
            e.printStackTrace()
            emit(
                DataState.error<List<SalesOrder>>(
                    response = Response(
                        message = "Unable to update the cache.",
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Error()
                    )
                )
            )
        }

        val successList = cache.searchSaleOderWithMedicine(
            query = query,
            status = status,
            page = page
        ).map { it.toSalesOder() }

        val failureList = cache.searchFailureSalesOderWithMedicine(
            query = query,
            status = status
        ).map {
            it.toSalesOder()
        }





        emit(DataState.data(response = null, data = marge(successList, failureList)))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}