package com.devscore.digital_pharmacy.business.datasource.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devscore.digital_pharmacy.business.datasource.cache.account.AccountDao
import com.devscore.digital_pharmacy.business.datasource.cache.account.AccountEntity
import com.devscore.digital_pharmacy.business.datasource.cache.auth.AuthTokenDao
import com.devscore.digital_pharmacy.business.datasource.cache.auth.AuthTokenEntity
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.GlobalMedicineDao
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.GlobalMedicineEntity

@Database(entities = [
    AuthTokenEntity::class,
    AccountEntity::class,
    GlobalMedicineEntity::class
], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao

    abstract fun getAccountPropertiesDao(): AccountDao

    abstract fun getGlobalMedicineDao() : GlobalMedicineDao


    companion object{
        val DATABASE_NAME: String = "digital_pharmacy"
    }
}