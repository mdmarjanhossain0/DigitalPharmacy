package com.devscore.digital_pharmacy.di.purchases

import com.devscore.digital_pharmacy.business.datasource.cache.AppDatabase
import com.devscore.digital_pharmacy.business.datasource.cache.purchases.PurchasesDao
import com.devscore.digital_pharmacy.business.datasource.cache.sales.SalesDao
import com.devscore.digital_pharmacy.business.datasource.network.purchases.PurchasesApiService
import com.devscore.digital_pharmacy.business.datasource.network.sales.SalesApiService
import com.devscore.digital_pharmacy.business.interactors.purchases.CreatePurchasesOrderInteractor
import com.devscore.digital_pharmacy.business.interactors.purchases.PurchasesCompleted
import com.devscore.digital_pharmacy.business.interactors.purchases.SearchPurchasesOrder
import com.devscore.digital_pharmacy.business.interactors.sales.CreateSalesOderInteractor
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
object PurchasesModule {

    @Singleton
    @Provides
    fun providePurchasesDao (
        database: AppDatabase
    ) : PurchasesDao {
        return database.getPurchasesDao()
    }


    @Singleton
    @Provides
    fun providePurchasesApiService (retrofitBuilder: Retrofit.Builder): PurchasesApiService {
        return retrofitBuilder
            .build()
            .create(PurchasesApiService::class.java)
    }


    @Singleton
    @Provides
    fun providePurchasesSearch (
        service : PurchasesApiService,
        cache : PurchasesDao
    ) : SearchPurchasesOrder {
        return SearchPurchasesOrder(
            service = service,
            cache = cache
        )
    }

    @Singleton
    @Provides
    fun provideCreatePurchasesOrder (
        service : PurchasesApiService,
        cache : PurchasesDao
    ) : CreatePurchasesOrderInteractor {
        return CreatePurchasesOrderInteractor(
            service = service,
            cache = cache
        )
    }

    @Singleton
    @Provides
    fun providePurchasesCompleted (
        service : PurchasesApiService,
        cache : PurchasesDao
    ) : PurchasesCompleted {
        return PurchasesCompleted(
            service = service,
            cache = cache
        )
    }
}