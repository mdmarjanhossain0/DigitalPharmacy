package com.devscore.digital_pharmacy.business.interactors.purchases

import android.util.Log
import com.devscore.digital_pharmacy.business.datasource.cache.purchases.PurchasesDao
import com.devscore.digital_pharmacy.business.datasource.cache.sales.SalesDao
import com.devscore.digital_pharmacy.business.datasource.network.handleUseCaseException
import com.devscore.digital_pharmacy.business.datasource.network.purchases.PurchasesApiService
import com.devscore.digital_pharmacy.business.datasource.network.purchases.network_response.toPurchasesOrder
import com.devscore.digital_pharmacy.business.datasource.network.sales.SalesApiService
import com.devscore.digital_pharmacy.business.datasource.network.sales.network_response.toSalesOrder
import com.devscore.digital_pharmacy.business.domain.models.*
import com.devscore.digital_pharmacy.business.domain.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class CreatePurchasesOrderInteractor (
    private val service : PurchasesApiService,
    private val cache : PurchasesDao
) {

    private val TAG: String = "AppDebug"

    fun execute(
        authToken: AuthToken?,
        createPurchasesOder: CreatePurchasesOder
    ): Flow<DataState<PurchasesOrder>> = flow {

        emit(DataState.loading<PurchasesOrder>())
        if(authToken == null){
            throw Exception(ErrorHandling.ERROR_AUTH_TOKEN_INVALID)
        }

        if (createPurchasesOder.vendor == -1) {
            createPurchasesOder.vendor = null
        }

        try{
            Log.d(TAG, "Call Api Section")
            val purchasesOrder = service.createPurchasesOder(
                "Token ${authToken.token}",
                createPurchasesOder
            ).toPurchasesOrder()


            try{
                cache.insertPurchasesOrder(purchasesOrder.toPurchasesOrderEntity())
                for (medicine in purchasesOrder.toPurchasesOrderMedicines()) {
                    cache.insertPurchasesOrderMedicine(medicine)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }

            emit(
                DataState.data(response = Response(
                    message = "Successfully Uploaded.",
                    uiComponentType = UIComponentType.Dialog(),
                    messageType = MessageType.Success()
                ), data = purchasesOrder))


        } catch (e: Exception){
            e.printStackTrace()

            try{
                cache.insertFailurePurchasesOrder(createPurchasesOder.toPurchasesOrder().toFailurePurchasesOrderEntity())
                for (failureMedicine in createPurchasesOder.toPurchasesOrder().toFailurePurchasesOrderMedicineEntity()) {
                    cache.insertFailurePurchasesOrderMedicine(failureMedicine)
                }
            } catch (e: Exception){
                e.printStackTrace()
            }


            emit(
                DataState.error<PurchasesOrder>(
                    response = Response(
                        message = "Unable to create Sales Oder. Please be careful and don't uninstall or log out",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    )
                )
            )
        }



        val purchasesOrder = createPurchasesOder.toPurchasesOrder()


        emit(
            DataState.data(response = Response(
                message = "Unable to create supplier. Please be careful and don't uninstall or log out",
                uiComponentType = UIComponentType.None(),
                messageType = MessageType.Error()
            ), data = purchasesOrder))

    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}