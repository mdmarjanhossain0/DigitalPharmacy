package com.devscore.digital_pharmacy.business.datasource.network.supplier

import com.devscore.digital_pharmacy.business.datasource.network.supplier.network_response.SupplierCreateResponse
import com.devscore.digital_pharmacy.business.domain.models.Supplier
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SupplierApiService {


    @POST("account/createsupplier")
    suspend fun createSupplier(
        @Header("Authorization") authorization: String,
        @Body supplier : Supplier
    ) : SupplierCreateResponse

}