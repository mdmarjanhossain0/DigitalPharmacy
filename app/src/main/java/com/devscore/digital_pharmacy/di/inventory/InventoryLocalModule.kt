package com.devscore.digital_pharmacy.di.inventory

import com.devscore.digital_pharmacy.business.datasource.cache.AppDatabase
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.LocalMedicineDao
import com.devscore.digital_pharmacy.business.datasource.network.inventory.InventoryApiService
import com.devscore.digital_pharmacy.business.interactors.inventory.local.SearchLocalMedicine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@Module
@InstallIn(SingletonComponent::class)
object InventoryLocalModule {

    @Singleton
    @Provides
    fun provideLocalMedicineDao(
        database: AppDatabase
    ) : LocalMedicineDao {
        return database.getLocalMedicineDao()
    }


    @Singleton
    @Provides
    fun provideSearchLocalMedicine(
        service: InventoryApiService,
        localMedicineDao : LocalMedicineDao
    ) : SearchLocalMedicine {
        return SearchLocalMedicine(
            service = service,
            cache = localMedicineDao
        )
    }
}