package com.devscore.digital_pharmacy.business.datasource.cache.supplier

import androidx.room.*
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.global.GlobalMedicineEntity
import com.devscore.digital_pharmacy.business.domain.util.Constants

@Dao
interface SupplierDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupplier(supplier : SupplierEntity): Long


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFailureSupplier(failureSupplierEntity: FailureSupplierEntity): Long


    @Delete
    suspend fun deleteSupplier(supplier: SupplierEntity)

    @Query("DELETE FROM AppClientVendor WHERE pk = :pk")
    suspend fun deleteSupplier(pk : Int)


    @Delete
    suspend fun deleteFailureSupplier(failureSupplierEntity: FailureSupplierEntity)

    @Query("DELETE FROM FailureAppClientVendor WHERE room_id = :room_id")
    suspend fun deleteSupplier(room_id : Long)



    @Query("""
        SELECT * FROM AppClientVendor 
        WHERE company_name LIKE '%' || :query || '%' 
        OR agent_name LIKE '%' || :query || '%' 
        OR email LIKE '%' || :query || '%' 
        OR mobile LIKE '%' || :query || '%' 
        OR whatsapp LIKE '%' || :query || '%' 
        OR facebook LIKE '%' || :query || '%' 
        OR imo LIKE '%' || :query || '%' 
        OR address LIKE '%' || :query || '%' 
        ORDER BY updated_at DESC LIMIT (:page * :pageSize)
        """)
    suspend fun searchAllSupplier (
        query: String,
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): List<SupplierEntity>

    @Query("""
        SELECT * FROM FailureAppClientVendor 
        WHERE company_name LIKE '%' || :query || '%' 
        OR agent_name LIKE '%' || :query || '%' 
        OR email LIKE '%' || :query || '%' 
        OR mobile LIKE '%' || :query || '%' 
        OR whatsapp LIKE '%' || :query || '%' 
        OR facebook LIKE '%' || :query || '%' 
        OR imo LIKE '%' || :query || '%' 
        OR address LIKE '%' || :query || '%'
        """)
    suspend fun searchAllFailureSupplier (
        query: String
    ): List<FailureSupplierEntity>
}