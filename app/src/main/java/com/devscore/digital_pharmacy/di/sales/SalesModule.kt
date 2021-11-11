package com.devscore.digital_pharmacy.di.sales

import com.devscore.digital_pharmacy.business.datasource.cache.AppDatabase
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.LocalMedicineDao
import com.devscore.digital_pharmacy.business.datasource.cache.sales.SalesDao
import com.devscore.digital_pharmacy.business.datasource.network.inventory.InventoryApiService
import com.devscore.digital_pharmacy.business.datasource.network.sales.SalesApiService
import com.devscore.digital_pharmacy.business.interactors.inventory.local.SearchLocalMedicine
import com.devscore.digital_pharmacy.business.interactors.sales.CreateSalesOderInteractor
import com.devscore.digital_pharmacy.business.interactors.sales.SalesCompleted
import com.devscore.digital_pharmacy.business.interactors.sales.SearchSalesOder
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
object SalesModule {

    @Singleton
    @Provides
    fun provideSalesDao(
        database: AppDatabase
    ) : SalesDao {
        return database.getSalesDao()
    }


    @Singleton
    @Provides
    fun provideSalesApiService (retrofitBuilder: Retrofit.Builder): SalesApiService {
        return retrofitBuilder
            .build()
            .create(SalesApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideSearchSales (
        service : SalesApiService,
        cache : SalesDao
    ) : SearchSalesOder {
        return SearchSalesOder(
            service = service,
            cache = cache
        )
    }

    @Singleton
    @Provides
    fun provideCreateSalesOrderInteractor (
        service : SalesApiService,
        cache : SalesDao
    ) : CreateSalesOderInteractor {
        return CreateSalesOderInteractor(
            service = service,
            cache = cache
        )
    }


    @Singleton
    @Provides
    fun provideSalesCompleted (
        service : SalesApiService,
        cache : SalesDao
    ) : SalesCompleted {
        return SalesCompleted(
            service = service,
            cache = cache
        )
    }
}