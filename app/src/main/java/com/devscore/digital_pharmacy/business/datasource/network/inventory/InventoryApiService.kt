package com.devscore.digital_pharmacy.business.datasource.network.inventory

import com.devscore.digital_pharmacy.business.datasource.network.inventory.network_responses.AddMedicineResponse
import com.devscore.digital_pharmacy.business.datasource.network.inventory.network_responses.DeleteResponse
import com.devscore.digital_pharmacy.business.datasource.network.inventory.network_responses.GlobalMedicineResponse
import com.devscore.digital_pharmacy.business.datasource.network.inventory.network_responses.LocalMedicineResponse
import com.devscore.digital_pharmacy.business.domain.models.AddMedicine
import retrofit2.http.*

interface InventoryApiService {



    @GET("inventory/globalmedicine")
    suspend fun searchAllListGlobalMedicine(
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
        @Query("page") page: Int
    ) : GlobalMedicineResponse

    @GET("inventory/globalmedicine")
    suspend fun searchByBrandNameGlobalMedicineLIst(
        @Header("Authorization") authorization: String,
        @Query("brand") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ) : GlobalMedicineResponse

    @GET("inventory/globalmedicine")
    suspend fun searchByGenericNameGlobalMedicineLIst(
        @Header("Authorization") authorization: String,
        @Query("generic") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ) : GlobalMedicineResponse


    @GET("inventory/globalmedicine")
    suspend fun searchByIndicationGlobalMedicineLIst(
        @Header("Authorization") authorization: String,
        @Query("indication") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ) : GlobalMedicineResponse


    @GET("inventory/globalmedicine")
    suspend fun searchByCompanyGlobalMedicineLIst(
        @Header("Authorization") authorization: String,
        @Query("company") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ) : GlobalMedicineResponse





    @GET("inventory/localmedicine")
    suspend fun searchLocalMedicineList(
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
        @Query("page") page: Int
    ) : LocalMedicineResponse


    @DELETE("inventory/{}")
    suspend fun deleteMedicine(
        @Header("Authorization") authorization: String,
        id : Int
    ) : DeleteResponse


    @POST("inventory/addmedicine")
    suspend fun addMedicine(
        @Header("Authorization") authorization: String,
        @Body medicine : AddMedicine
    ) : AddMedicineResponse
}