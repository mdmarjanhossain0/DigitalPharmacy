package com.devscore.digital_pharmacy.business.datasource.network.purchases

import com.devscore.digital_pharmacy.business.datasource.network.purchases.network_response.CreatePurchasesOderResponse
import com.devscore.digital_pharmacy.business.datasource.network.purchases.network_response.PurchasesOderListResponse
import com.devscore.digital_pharmacy.business.domain.models.CreatePurchasesOder
import retrofit2.http.*

interface PurchasesApiService {


    @POST("purchases/purchasesoder")
    suspend fun createPurchasesOder (
        @Header("Authorization") authorization: String,
        @Body createPurchasesOder : CreatePurchasesOder
    ) : CreatePurchasesOderResponse


    @GET("purchases/purchaseslist")
    suspend fun searchPurchasesOder (
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
        @Query("page") page: Int
    ) : PurchasesOderListResponse

}