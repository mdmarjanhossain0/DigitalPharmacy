package com.devscore.digital_pharmacy.di.customer

import com.devscore.digital_pharmacy.business.datasource.cache.AppDatabase
import com.devscore.digital_pharmacy.business.datasource.cache.customer.CustomerDao
import com.devscore.digital_pharmacy.business.datasource.cache.supplier.SupplierDao
import com.devscore.digital_pharmacy.business.datasource.network.customer.CustomerApiService
import com.devscore.digital_pharmacy.business.datasource.network.supplier.SupplierApiService
import com.devscore.digital_pharmacy.business.interactors.customer.CreateCustomerInteractor
import com.devscore.digital_pharmacy.business.interactors.customer.SearchCustomer
import com.devscore.digital_pharmacy.business.interactors.supplier.CreateSupplierInteractor
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
object CustomerModule {

    @Singleton
    @Provides
    fun provideCustomerDao (
        database: AppDatabase
    ) : CustomerDao {
        return database.getCustomerDao()
    }


    @Singleton
    @Provides
    fun provideCustomerApiService (retrofitBuilder: Retrofit.Builder): CustomerApiService {
        return retrofitBuilder
            .build()
            .create(CustomerApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideSearchCustomer (
        service : CustomerApiService,
        cache : CustomerDao
    ) : SearchCustomer {
        return SearchCustomer(
            service = service,
            cache = cache
        )
    }

    @Singleton
    @Provides
    fun provideCustomerInteractor (
        service: CustomerApiService,
        cache: CustomerDao
    ) : CreateCustomerInteractor {
        return CreateCustomerInteractor(
            service = service,
            cache = cache
        )
    }
}