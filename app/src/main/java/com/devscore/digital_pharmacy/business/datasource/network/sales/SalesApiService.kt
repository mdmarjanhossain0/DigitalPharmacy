package com.devscore.digital_pharmacy.business.datasource.network.sales

import com.devscore.digital_pharmacy.business.datasource.network.sales.network_response.CreateSalesOderResponse
import com.devscore.digital_pharmacy.business.datasource.network.sales.network_response.SalesOderListResponse
import com.devscore.digital_pharmacy.business.domain.models.CreateSalesOder
import retrofit2.http.*

interface SalesApiService {


    @POST("sales/salesoder")
    suspend fun createSalesOder (
        @Header("Authorization") authorization: String,
        @Body createSalesOder: CreateSalesOder
    ) : CreateSalesOderResponse


    @GET("account/supplierlist")
    suspend fun searchSalesOder (
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
        @Query("page") page: Int
    ) : SalesOderListResponse

}