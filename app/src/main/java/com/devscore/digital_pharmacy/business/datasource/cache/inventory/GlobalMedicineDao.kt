package com.devscore.digital_pharmacy.business.datasource.cache.inventory

import androidx.room.*
import com.devscore.digital_pharmacy.business.domain.util.Constants.Companion.PAGINATION_PAGE_SIZE

@Dao
interface GlobalMedicineDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blogPost: GlobalMedicineEntity): Long

    @Delete
    suspend fun deleteBlogPost(blogPost: GlobalMedicineEntity)

    @Query("DELETE FROM GlobalMedicine WHERE id = :id")
    suspend fun deleteBlogPost(id: Int)

    @Query(" SELECT * FROM GlobalMedicine LIMIT (:page * :pageSize)")
    suspend fun getAllGlobalMedicine(
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<GlobalMedicineEntity>


    @Query("SELECT * FROM GlobalMedicine WHERE id = :id ")
    suspend fun getGlobalMedicine(id: Int): GlobalMedicineEntity?
}