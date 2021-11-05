package com.devscore.digital_pharmacy.business.datasource.network.inventory

import android.util.Log
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
//        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ) : GlobalMedicineResponse

    @GET("inventory/globalmedicinebrandname")
    suspend fun searchByBrandNameGlobalMedicineList(
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
//        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ) : GlobalMedicineResponse

    @GET("inventory/globalmedicinegeneric")
    suspend fun searchByGenericNameGlobalMedicineList(
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
//        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ) : GlobalMedicineResponse


    @GET("inventory/globalmedicineindication")
    suspend fun searchByIndicationGlobalMedicineList(
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
//        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ) : GlobalMedicineResponse


    @GET("inventory/globalmedicinemanufacturer")
    suspend fun searchByCompanyGlobalMedicineList(
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
//        @Query("ordering") ordering: String,
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





suspend fun InventoryApiService.searchGlobalMedicine(
    authorization: String,
    query: String,
    page: Int,
    action : String
) : GlobalMedicineResponse {
    when(action) {
        InventoryUtils.BRAND_NAME -> {
            Log.d("AppDebug", "Network Brand Name")
            return searchByBrandNameGlobalMedicineList(
                authorization = authorization,
                query = query,
                page = page
            )
        }

        InventoryUtils.GENERIC -> {
            Log.d("AppDebug", "Network Generic")
            return searchByGenericNameGlobalMedicineList(
                authorization = authorization,
                query = query,
                page = page
            )
        }

        InventoryUtils.INDICATION -> {
            Log.d("AppDebug", "Network Indication")
            return searchByIndicationGlobalMedicineList(
                authorization = authorization,
                query = query,
                page = page
            )
        }

        InventoryUtils.SYMPTOM -> {
            Log.d("AppDebug", "Network Symptom")
            return searchByCompanyGlobalMedicineList(
                authorization = authorization,
                query = query,
                page = page
            )
        }

        else -> {
            Log.d("AppDebug", "Network Global")
            return searchAllListGlobalMedicine(
                authorization = authorization,
                query = query,
                page = page
            )
        }
    }
}