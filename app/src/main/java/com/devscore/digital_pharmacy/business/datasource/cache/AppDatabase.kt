package com.devscore.digital_pharmacy.business.datasource.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devscore.digital_pharmacy.business.datasource.cache.account.AccountDao
import com.devscore.digital_pharmacy.business.datasource.cache.account.AccountEntity
import com.devscore.digital_pharmacy.business.datasource.cache.auth.AuthTokenDao
import com.devscore.digital_pharmacy.business.datasource.cache.auth.AuthTokenEntity
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.global.GlobalMedicineDao
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.global.GlobalMedicineEntity
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.LocalMedicineDao
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.LocalMedicineEntity
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.LocalMedicineUnitsEntity

@Database(entities = [
    AuthTokenEntity::class,
    AccountEntity::class,
    GlobalMedicineEntity::class,
    LocalMedicineEntity::class,
    LocalMedicineUnitsEntity::class
], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao

    abstract fun getAccountPropertiesDao(): AccountDao

    abstract fun getGlobalMedicineDao() : GlobalMedicineDao

    abstract fun getLocalMedicineDao() : LocalMedicineDao


    companion object{
        val DATABASE_NAME: String = "digital_pharmacy"
    }
}