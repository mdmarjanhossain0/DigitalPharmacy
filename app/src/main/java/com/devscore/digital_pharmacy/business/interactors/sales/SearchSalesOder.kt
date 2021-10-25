package com.devscore.digital_pharmacy.business.interactors.sales

import android.util.Log
import com.devscore.digital_pharmacy.business.datasource.cache.sales.SalesDao
import com.devscore.digital_pharmacy.business.datasource.cache.sales.toSalesOder
import com.devscore.digital_pharmacy.business.datasource.network.handleUseCaseException
import com.devscore.digital_pharmacy.business.datasource.network.sales.SalesApiService
import com.devscore.digital_pharmacy.business.datasource.network.sales.toSalesOder
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
    ): Flow<DataState<List<SalesOder>>> = flow {
        emit(DataState.loading<List<SalesOder>>())
        if(authToken == null){
            throw Exception(ErrorHandling.ERROR_AUTH_TOKEN_INVALID)
        }

        try{
            Log.d(TAG, "Call Api Section")
            val oderList = service.searchSalesOder(
                "Token ${authToken.token}",
                query = query,
                page = page
            ).results.map {
                Log.d(TAG, "looping toLocalMedicine")
                it.toSalesOder()
            }

            for(oder in oderList){
                try{
                    Log.d(TAG, "Caching size" + oderList.size.toString())
                    cache.insertSalesOder(oder.toSalesOderEntity())
                    for (medicine in oder.toSalesOderMedicinesEntity()) {
                        cache.insertSaleOderMedicine(medicine)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
            emit(
                DataState.error<List<SalesOder>>(
                    response = Response(
                        message = "Unable to update the cache.",
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Error()
                    )
                )
            )
        }

        val localMedicine = cache.searchSaleOderWithMedicine(
            query = query,
            page = page
        ).map { it.toSalesOder() }

        val failureMedicine = cache.searchFailureSalesOderWithMedicine(
            query = query,
        ).map {
            it.toSalesOder()
        }





        emit(DataState.data(response = null, data = marge(localMedicine, failureMedicine)))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}

fun marge(successList: List<SalesOder>, failureList : List<SalesOder>) : List<SalesOder> {
    val allMedicine  = mutableListOf<SalesOder>()
    allMedicine.addAll(successList)
    allMedicine.addAll(failureList)
    return allMedicine
}