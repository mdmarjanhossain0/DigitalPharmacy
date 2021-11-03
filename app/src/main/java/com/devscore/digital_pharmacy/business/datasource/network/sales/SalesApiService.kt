package com.devscore.digital_pharmacy.business.datasource.network.sales

import com.devscore.digital_pharmacy.business.datasource.network.sales.network_response.CreateSalesOrderResponse
import com.devscore.digital_pharmacy.business.datasource.network.sales.network_response.SalesOderListResponse
import com.devscore.digital_pharmacy.business.domain.models.CreateSalesOrder
import retrofit2.http.*

interface SalesApiService {


    @POST("sales/salesoder")
    suspend fun createSalesOder (
        @Header("Authorization") authorization: String,
        @Body createSalesOder: CreateSalesOrder
    ) : CreateSalesOrderResponse


    @GET("sales/saleslist")
    suspend fun searchSalesOder (
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
        @Query("status") status : Int,
        @Query("page") page: Int
    ) : SalesOderListResponse


    @PUT("sales/statuscompleted/{pk}")
    suspend fun salesCompleted (
        @Header("Authorization") authorization: String,
        @Path("pk") pk : Int
    ) : SalesOrderDto


}