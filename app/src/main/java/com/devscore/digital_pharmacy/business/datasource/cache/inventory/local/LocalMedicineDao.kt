package com.devscore.digital_pharmacy.business.datasource.cache.inventory.local

import androidx.room.*
import com.devscore.digital_pharmacy.business.domain.util.Constants

@Dao
interface LocalMedicineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalMedicine(localMedicine : LocalMedicineEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalMedicineUnit(localMedicineUnit : LocalMedicineUnitsEntity): Long

    @Delete
    suspend fun deleteLocalMedicine(localMedicine : LocalMedicineEntity)

    @Query("DELETE FROM LocalMedicine WHERE id = :id")
    suspend fun deleteLocalMedicine(id: Int)

    @Query(" SELECT * FROM LocalMedicine LIMIT (:page * :pageSize)")
    suspend fun getAllLocalMedicineWithUnits(
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): List<LocalMedicineWithUnits>

    @Query(" SELECT * FROM LocalMedicine LIMIT (:page * :pageSize)")
    suspend fun getAllLocalMedicine(
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): List<LocalMedicineEntity>


    @Query("SELECT * FROM LocalMedicine WHERE id = :id ")
    suspend fun getGlobalMedicineWithUnits(id: Int): LocalMedicineWithUnits?
}