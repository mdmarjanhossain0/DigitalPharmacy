package com.devscore.digital_pharmacy.business.interactors.customer

import android.util.Log
import com.devscore.digital_pharmacy.business.datasource.cache.customer.CustomerDao
import com.devscore.digital_pharmacy.business.datasource.cache.supplier.SupplierDao
import com.devscore.digital_pharmacy.business.datasource.network.customer.CustomerApiService
import com.devscore.digital_pharmacy.business.datasource.network.customer.network_response.toCustomer
import com.devscore.digital_pharmacy.business.datasource.network.handleUseCaseException
import com.devscore.digital_pharmacy.business.datasource.network.supplier.SupplierApiService
import com.devscore.digital_pharmacy.business.datasource.network.supplier.network_response.toSupplier
import com.devscore.digital_pharmacy.business.domain.models.*
import com.devscore.digital_pharmacy.business.domain.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class CreateCustomerInteractor (
    private val service : CustomerApiService,
    private val cache : CustomerDao
) {

    private val TAG: String = "AppDebug"

    fun execute(
        authToken: AuthToken?,
        createCustomer: CreateCustomer
    ): Flow<DataState<Customer>> = flow {

        emit(DataState.loading<Customer>())
        if(authToken == null){
            throw Exception(ErrorHandling.ERROR_AUTH_TOKEN_INVALID)
        }

        try{
            Log.d(TAG, "Call Api Section")
            val customer = service.createCustomer(
                "Token ${authToken.token}",
                createCustomer
            ).toCustomer()


            try{
                cache.insertCustomer(customer.toCustomerEntity())
            }catch (e: Exception){
                e.printStackTrace()
            }

            emit(
                DataState.data(response = Response(
                    message = "Successfully Uploaded.",
                    uiComponentType = UIComponentType.Dialog(),
                    messageType = MessageType.Success()
                ), data = customer))


        } catch (e: Exception){
            e.printStackTrace()

            try{
                cache.insertFailureCustomer(createCustomer.toCustomer().toCustomerFailureEntity())
            } catch (e: Exception){
                e.printStackTrace()
            }


            emit(
                DataState.error<Customer>(
                    response = Response(
                        message = "Unable to create supplier. Please be careful and don't uninstall or log out",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    )
                )
            )
        }


        val state = createCustomer.toCustomer()

        emit(
            DataState.data(response = Response(
                message = "Unable to create supplier. Please be careful and don't uninstall or log out",
                uiComponentType = UIComponentType.None(),
                messageType = MessageType.Error()
            ), data = state))

    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}