package com.devscore.digital_pharmacy.business.datasource.cache.sales

import androidx.room.*
import com.devscore.digital_pharmacy.business.domain.util.Constants

@Dao
interface SalesDao {

    // Insert

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSalesOder(salesOder : SalesOrderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaleOderMedicine(salesOderMedicine : SalesOrderMedicineEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFailureSalesOder(failureSalesOder : FailureSalesOrderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFailureSalesOderMedicine(failureSalesOderMedicine : FailureSalesOrderMedicineEntity): Long





    // Delete


    @Delete
    suspend fun deleteSalesOder(salesOder : SalesOrderEntity)

    @Delete
    suspend fun deleteFailureSalesOder(failureSalesOder : FailureSalesOrderEntity)






    @Query("DELETE FROM SalesOrder WHERE pk = :pk")
    suspend fun deleteSalesOder(pk : Int)


    @Query("DELETE FROM FailureSalesOrder WHERE room_id = :room_id")
    suspend fun deleteFailureSalesOder(room_id : Long)









    // Complex Query

    @Transaction
    @Query("""
        SELECT * FROM SalesOrder 
        WHERE customer LIKE '%' || :query || '%' 
        OR pk LIKE '%' || :query || '%'
        LIMIT (:page * :pageSize)
        """)
    suspend fun searchSaleOderWithMedicine(
        query: String,
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): List<SalesOderWithMedicine>



    @Transaction
    @Query("""
        SELECT * FROM FailureSalesOrder 
        WHERE customer LIKE '%' || :query || '%'
        """)
    suspend fun searchFailureSalesOderWithMedicine(
        query: String
    ): List<FailureSalesOrderWithMedicine>
}