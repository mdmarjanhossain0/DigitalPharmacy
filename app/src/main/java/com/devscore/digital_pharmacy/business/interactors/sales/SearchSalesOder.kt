package com.devscore.digital_pharmacy.business.interactors.sales

import android.util.Log
import com.devscore.digital_pharmacy.business.datasource.cache.sales.SalesDao
import com.devscore.digital_pharmacy.business.datasource.cache.sales.toSalesOder
import com.devscore.digital_pharmacy.business.datasource.network.handleUseCaseException
import com.devscore.digital_pharmacy.business.datasource.network.sales.SalesApiService
import com.devscore.digital_pharmacy.business.datasource.network.sales.toSalesOrder
import com.devscore.digital_pharmacy.business.domain.models.*
import com.devscore.digital_pharmacy.business.domain.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class SearchSalesOder(
    private val service : SalesApiService,
    private val cache : SalesDao
) {

    private val TAG: String = "AppDebug"

    fun execute(
        authToken: AuthToken?,
        query: String,
        page: Int
    ): Flow<DataState<List<SalesOrder>>> = flow {
        emit(DataState.loading<List<SalesOrder>>())
        if(authToken == null){
            throw Exception(ErrorHandling.ERROR_AUTH_TOKEN_INVALID)
        }

        try{
            Log.d(TAG, "Call Api Section")
            val result = service.searchSalesOder(
                "Token ${authToken.token}",
                query = query,
                page = page
            )

            Log.d(TAG, result.toString())

            val oderList = result.results.map {
                Log.d(TAG, "looping toLocalMedicine")
                it.toSalesOrder()
            }

            for(oder in oderList){
                try{
                    Log.d(TAG, "Caching size" + oderList.size.toString())
                    cache.insertSalesOder(oder.toSalesOrderEntity())
                    for (medicine in oder.toSalesOrderMedicinesEntity()) {
                        cache.insertSaleOderMedicine(medicine)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
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
            page = page
        ).map { it.toSalesOder() }

        val failureList = cache.searchFailureSalesOderWithMedicine(
            query = query,
        ).map {
            it.toSalesOder()
        }





        emit(DataState.data(response = null, data = marge(successList, failureList)))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}

fun marge(successList: List<SalesOrder>, failureList : List<SalesOrder>) : List<SalesOrder> {
    val allMedicine  = mutableListOf<SalesOrder>()
    allMedicine.addAll(successList)
    allMedicine.addAll(failureList)
    return allMedicine
}