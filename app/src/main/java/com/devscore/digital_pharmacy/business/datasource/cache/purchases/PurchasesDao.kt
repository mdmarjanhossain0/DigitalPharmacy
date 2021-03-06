package com.devscore.digital_pharmacy.business.datasource.cache.purchases

import androidx.room.*
import com.devscore.digital_pharmacy.business.datasource.cache.sales.*
import com.devscore.digital_pharmacy.business.domain.util.Constants

@Dao
interface PurchasesDao {

    // Insert

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchasesOrder(order : PurchasesOrderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchasesOrderMedicine(orderMedicine : PurchasesOrderMedicineEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFailurePurchasesOrder(failureOrder : FailurePurchasesOrderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFailurePurchasesOrderMedicine(failureOrderMedicine : FailurePurchasesOrderMedicineEntity): Long





    // Delete


    @Delete
    suspend fun deletePurchasesOrder(order : PurchasesOrderEntity)

    @Delete
    suspend fun deleteFailurePurchasesOrder(failureOrder : FailurePurchasesOrderEntity)






    @Query("DELETE FROM PurchasesOrder WHERE pk = :pk")
    suspend fun deletePurchasesOrder(pk : Int)


    @Query("DELETE FROM FailurePurchasesOrder WHERE room_id = :room_id")
    suspend fun deleteFailurePurchasesOrder(room_id : Long)









    // Complex Query


//    SELECT * FROM PurchasesOrder
//    WHERE vendor LIKE '%' || :query || '%'
//    OR pk LIKE '%' || :query || '%'
//    AND status = :status
//    LIMIT (:page * :pageSize)

    @Transaction
    @Query("""
        SELECT * FROM PurchasesOrder 
        WHERE status = :status
        AND (vendor LIKE '%' || :query || '%' 
        OR pk LIKE '%' || :query || '%')
        LIMIT (:page * :pageSize)
        """)
    suspend fun searchPurchasesOrderWithMedicine(
        query: String,
        page: Int,
        status : Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): List<PurchasesOrderWithMedicine>


    @Transaction
    @Query("""
        SELECT * FROM FailurePurchasesOrder 
        WHERE customer LIKE '%' || :query || '%'
        AND status = :status
        """)
    suspend fun searchFailurePurchasesOrderWithMedicine(
        query: String,
        status : Int,
    ): List<FailurePurchasesOrderWithMedicine>



    @Transaction
    @Query("""
        SELECT * FROM PurchasesOrder WHERE pk = :pk
        """)
    suspend fun getPurchasesOrder (
        pk : Int
    ): PurchasesOrderWithMedicine

    @Transaction
    @Query("""
        SELECT * FROM PurchasesOrder WHERE pk = :pk
        """)
    suspend fun getFailurePurchasesOrder (
        pk : Int
    ): PurchasesOrderWithMedicine
}