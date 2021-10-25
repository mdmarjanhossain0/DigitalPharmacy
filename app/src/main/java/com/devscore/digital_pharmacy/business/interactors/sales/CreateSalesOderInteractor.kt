package com.devscore.digital_pharmacy.business.interactors.sales

import android.util.Log
import com.devscore.digital_pharmacy.business.datasource.cache.sales.SalesDao
import com.devscore.digital_pharmacy.business.datasource.network.handleUseCaseException
import com.devscore.digital_pharmacy.business.datasource.network.sales.SalesApiService
import com.devscore.digital_pharmacy.business.datasource.network.sales.network_response.toSalesOrder
import com.devscore.digital_pharmacy.business.domain.models.*
import com.devscore.digital_pharmacy.business.domain.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class CreateSalesOderInteractor (
    private val service : SalesApiService,
    private val cache : SalesDao
) {

    private val TAG: String = "AppDebug"

    fun execute(
        authToken: AuthToken?,
        createSalesOder: CreateSalesOrder
    ): Flow<DataState<SalesOrder>> = flow {

        emit(DataState.loading<SalesOrder>())
        if(authToken == null){
            throw Exception(ErrorHandling.ERROR_AUTH_TOKEN_INVALID)
        }

        try{
            Log.d(TAG, "Call Api Section")
            val salesOder = service.createSalesOder(
                "Token ${authToken.token}",
                createSalesOder
            ).toSalesOrder()


            try{
                cache.insertSalesOder(salesOder.toSalesOrderEntity())
                for (medicine in salesOder.toSalesOrderMedicinesEntity()) {
                    cache.insertSaleOderMedicine(medicine)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }

            emit(
                DataState.data(response = Response(
                    message = "Successfully Uploaded.",
                    uiComponentType = UIComponentType.Dialog(),
                    messageType = MessageType.Success()
                ), data = salesOder))


        } catch (e: Exception){
            e.printStackTrace()

            try{
                cache.insertFailureSalesOder(createSalesOder.toSalesOder().toFailureSalesOrderEntity())
                for (failureMedicine in createSalesOder.toSalesOder().toFailureSalesOderMedicineEntity()) {
                    cache.insertFailureSalesOderMedicine(failureMedicine)
                }
            } catch (e: Exception){
                e.printStackTrace()
            }


            emit(
                DataState.error<SalesOrder>(
                    response = Response(
                        message = "Unable to create Sales Oder. Please be careful and don't uninstall or log out",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    )
                )
            )
        }



        val stateSalesODer = createSalesOder.toSalesOder()


        emit(
            DataState.data(response = Response(
                message = "Unable to create supplier. Please be careful and don't uninstall or log out",
                uiComponentType = UIComponentType.None(),
                messageType = MessageType.Error()
            ), data = stateSalesODer))

    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}