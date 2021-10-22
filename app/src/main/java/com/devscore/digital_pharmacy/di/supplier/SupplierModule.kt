package com.devscore.digital_pharmacy.di.supplier

import com.devscore.digital_pharmacy.business.datasource.cache.AppDatabase
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.LocalMedicineDao
import com.devscore.digital_pharmacy.business.datasource.cache.supplier.SupplierDao
import com.devscore.digital_pharmacy.business.datasource.network.inventory.InventoryApiService
import com.devscore.digital_pharmacy.business.datasource.network.supplier.SupplierApiService
import com.devscore.digital_pharmacy.business.interactors.inventory.local.SearchLocalMedicine
import com.devscore.digital_pharmacy.business.interactors.supplier.SearchSupplier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.FlowPreview
import retrofit2.Retrofit
import javax.inject.Singleton

@FlowPreview
@Module
@InstallIn(SingletonComponent::class)
object SupplierModule {

    @Singleton
    @Provides
    fun provideSupplierDao(
        database: AppDatabase
    ) : SupplierDao {
        return database.getSupplierDao()
    }


    @Singleton
    @Provides
    fun provideSupplierApiService(retrofitBuilder: Retrofit.Builder): SupplierApiService {
        return retrofitBuilder
            .build()
            .create(SupplierApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideSearchSupplier (
        service : SupplierApiService,
        cache : SupplierDao
    ) : SearchSupplier {
        return SearchSupplier(
            service = service,
            cache = cache
        )
    }
}