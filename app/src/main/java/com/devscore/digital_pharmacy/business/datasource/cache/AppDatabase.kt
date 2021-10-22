package com.devscore.digital_pharmacy.business.datasource.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devscore.digital_pharmacy.business.datasource.cache.account.AccountDao
import com.devscore.digital_pharmacy.business.datasource.cache.account.AccountEntity
import com.devscore.digital_pharmacy.business.datasource.cache.auth.AuthTokenDao
import com.devscore.digital_pharmacy.business.datasource.cache.auth.AuthTokenEntity
import com.devscore.digital_pharmacy.business.datasource.cache.customer.CustomerDao
import com.devscore.digital_pharmacy.business.datasource.cache.customer.CustomerEntity
import com.devscore.digital_pharmacy.business.datasource.cache.customer.FailureCustomerEntity
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.global.GlobalMedicineDao
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.global.GlobalMedicineEntity
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.*
import com.devscore.digital_pharmacy.business.datasource.cache.supplier.FailureSupplierEntity
import com.devscore.digital_pharmacy.business.datasource.cache.supplier.SupplierDao
import com.devscore.digital_pharmacy.business.datasource.cache.supplier.SupplierEntity

@Database(entities = [
    AuthTokenEntity::class,
    AccountEntity::class,
    GlobalMedicineEntity::class,
    LocalMedicineEntity::class,
    LocalMedicineUnitsEntity::class,
    FailureMedicineEntity::class,
    FailureMedicineUnitEntity::class,
    SupplierEntity::class,
    FailureSupplierEntity::class,
    CustomerEntity::class,
    FailureCustomerEntity::class
], version = 6)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao

    abstract fun getAccountPropertiesDao(): AccountDao

    abstract fun getGlobalMedicineDao() : GlobalMedicineDao

    abstract fun getLocalMedicineDao() : LocalMedicineDao

    abstract fun getSupplierDao() : SupplierDao

    abstract fun getCustomerDao() : CustomerDao


    companion object{
        val DATABASE_NAME: String = "digital_pharmacy"
    }
}