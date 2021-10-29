package com.devscore.digital_pharmacy.business.datasource.network.customer

import com.devscore.digital_pharmacy.business.datasource.network.customer.network_response.CustomerCreateResponse
import com.devscore.digital_pharmacy.business.datasource.network.customer.network_response.CustomerListResponse
import com.devscore.digital_pharmacy.business.datasource.network.supplier.network_response.SupplierCreateResponse
import com.devscore.digital_pharmacy.business.datasource.network.supplier.network_response.SupplierListResponse
import com.devscore.digital_pharmacy.business.domain.models.CreateCustomer
import com.devscore.digital_pharmacy.business.domain.models.CreateSupplier
import retrofit2.http.*

interface CustomerApiService {


    @POST("account/createcustomer")
    suspend fun createCustomer (
        @Header("Authorization") authorization: String,
        @Body createCustomer : CreateCustomer
    ) : CustomerCreateResponse


    @GET("account/customerlist")
    suspend fun searchCustomer (
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
        @Query("page") page: Int
    ) : CustomerListResponse

}