package com.devscore.digital_pharmacy.business.datasource.cache.sales

import androidx.lifecycle.LiveData
import androidx.room.*
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.*
import com.devscore.digital_pharmacy.business.domain.util.Constants

@Dao
interface SalesDao {

    // Insert

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSalesOder(salesOder : SalesOderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaleOderMedicine(salesOderMedicine : SalesOderMedicineEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFailureSalesOder(failureSalesOder : FailureSalesOderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFailureSalesOderMedicine(failureSalesOderMedicine : FailureSalesOderMedicineEntity): Long





    // Delete


    @Delete
    suspend fun deleteSalesOder(salesOder : SalesOderEntity)

    @Delete
    suspend fun deleteFailureSalesOder(failureSalesOder : FailureSalesOderEntity)






    @Query("DELETE FROM SalesOder WHERE pk = :pk")
    suspend fun deleteSalesOder(pk : Int)


    @Query("DELETE FROM FailureSalesOder WHERE room_id = :room_id")
    suspend fun deleteFailureSalesOder(room_id : Long)









    // Complex Query


    @Query("""
        SELECT * FROM SalesOder 
        WHERE customer LIKE '%' || :query || '%' 
        OR pk LIKE '%' || :query || '%'
        LIMIT (:page * :pageSize)
        """)
    suspend fun searchSaleOderWithMedicine(
        query: String,
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): List<SalesOderWithMedicine>


    @Query("""
        SELECT * FROM FailureSalesOder 
        WHERE customer LIKE '%' || :query || '%'
        """)
    suspend fun searchFailureSalesOderWithMedicine(
        query: String
    ): List<FailureSalesOderWithMedicine>
}