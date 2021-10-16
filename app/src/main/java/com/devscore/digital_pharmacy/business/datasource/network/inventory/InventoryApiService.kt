package com.devscore.digital_pharmacy.business.datasource.network.inventory

import com.devscore.digital_pharmacy.business.datasource.network.inventory.network_responses.DeleteResponse
import com.devscore.digital_pharmacy.business.datasource.network.inventory.network_responses.GlobalMedicineResponse
import retrofit2.http.*

interface InventoryApiService {



    @GET("inventory/globalmedicinelist")
    suspend fun searchAllListGlobalMedicine(
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
        @Query("page") page: Int
    ) : GlobalMedicineResponse

    @GET("inventory/globalmedicinelist")
    suspend fun searchByBrandNameGlobalMedicineLIst(
        @Header("Authorization") authorization: String,
        @Query("brand") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ) : GlobalMedicineResponse

    @GET("inventory/globalmedicinelist")
    suspend fun searchByGenericNameGlobalMedicineLIst(
        @Header("Authorization") authorization: String,
        @Query("generic") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ) : GlobalMedicineResponse


    @GET("inventory/globalmedicinelist")
    suspend fun searchByIndicationGlobalMedicineLIst(
        @Header("Authorization") authorization: String,
        @Query("indication") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ) : GlobalMedicineResponse


    @GET("inventory/globalmedicinelist")
    suspend fun searchByCompanyGlobalMedicineLIst(
        @Header("Authorization") authorization: String,
        @Query("company") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ) : GlobalMedicineResponse





    @GET("inventory/globalmedicinelist")
    suspend fun searchLocalMedicineList(
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ) : GlobalMedicineResponse


    @DELETE("inventory/{}")
    suspend fun deleteMedicine(
        @Header("Authorization") authorization: String,
        id : Int
    ) : DeleteResponse


    @POST("inventory/addmedicine")
    suspend fun addMedicine(
        @Header("Authorization") authorization: String
    )
}