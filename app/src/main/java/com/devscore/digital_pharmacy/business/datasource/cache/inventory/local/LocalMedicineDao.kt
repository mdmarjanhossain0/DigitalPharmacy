package com.devscore.digital_pharmacy.business.datasource.cache.inventory.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.devscore.digital_pharmacy.business.domain.util.Constants
import com.devscore.digital_pharmacy.business.domain.util.Constants.Companion.PAGINATION_PAGE_SIZE

@Dao
interface LocalMedicineDao {

    // Insert

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalMedicine(localMedicine : LocalMedicineEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalMedicineUnit(localMedicineUnit : LocalMedicineUnitsEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFailureMedicine(localMedicine : FailureMedicineEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFailureMedicineUnit(localMedicineUnit : FailureMedicineUnitEntity): Long





    // Delete


    @Delete
    suspend fun deleteLocalMedicine(localMedicine : LocalMedicineEntity)

    @Delete
    suspend fun deleteFailureMedicine(localMedicine : FailureMedicineEntity)






    @Query("DELETE FROM LocalMedicine WHERE id = :id")
    suspend fun deleteLocalMedicine(id: Int)

    @Query("DELETE FROM FailureMedicine WHERE room_medicine_id = :room_medicine_id")
    suspend fun deleteFailureLocalMedicine(room_medicine_id : Long)

    @Query(" SELECT * FROM LocalMedicine LIMIT (:page * :pageSize)")
    suspend fun getAllLocalMedicineWithUnits(
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): List<LocalMedicineWithUnits>





    @Query(" SELECT * FROM LocalMedicine LIMIT (:page * :pageSize)")
    fun getAllLocalMedicine(
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): List<LocalMedicineEntity>





    @Query(" SELECT * FROM LocalMedicine WHERE id = :id")
   fun getRequestedData(
        id : Long = -1
    ): LiveData<List<LocalMedicineEntity>>

    @Query(" SELECT * FROM FailureMedicine")
    fun getSyncData(): List<FailureMedicineWithUnit>








    // Complex Query


    @Query("""
        SELECT * FROM LocalMedicine 
        WHERE brand_name LIKE '%' || :query || '%' 
        OR generic LIKE '%' || :query || '%' 
        OR manufacture LIKE '%' || :query || '%' 
        LIMIT (:page * :pageSize)
        """)
    suspend fun searchLocalMedicineWithUnitWithQuery(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<LocalMedicineWithUnits>


    @Query("""
        SELECT * FROM FailureMedicine 
        WHERE brand_name LIKE '%' || :query || '%' 
        OR generic LIKE '%' || :query || '%' 
        OR manufacture LIKE '%' || :query || '%'
        """)
    suspend fun searchFailureMedicineWithUnits(
        query: String
    ): List<FailureMedicineWithUnit>
}