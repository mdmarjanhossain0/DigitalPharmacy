package com.devscore.digital_pharmacy.business.datasource.network.supplier

import com.devscore.digital_pharmacy.business.datasource.network.supplier.network_response.SupplierCreateResponse
import com.devscore.digital_pharmacy.business.datasource.network.supplier.network_response.SupplierListResponse
import com.devscore.digital_pharmacy.business.domain.models.CreateSupplier
import com.devscore.digital_pharmacy.business.domain.models.Supplier
import retrofit2.http.*

interface SupplierApiService {


    @POST("account/createsupplier")
    suspend fun createSupplier (
        @Header("Authorization") authorization: String,
        @Body createSupplier : CreateSupplier
    ) : SupplierCreateResponse


    @GET("account/supplierlist")
    suspend fun searchSupplier (
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
        @Query("page") page: Int
    ) : SupplierListResponse

}