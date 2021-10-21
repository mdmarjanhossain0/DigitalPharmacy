package com.devscore.digital_pharmacy.di.inventory

import android.content.Context
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.LocalMedicineDao
import com.devscore.digital_pharmacy.business.datasource.network.inventory.InventoryApiService
import com.devscore.digital_pharmacy.business.interactors.inventory.AddMedicineInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@Module
@InstallIn(SingletonComponent::class)
object InventoryAddMedicineModule {


    @Singleton
    @Provides
    fun provideAddMedicineInteractior(
        service: InventoryApiService,
        localMedicineDao : LocalMedicineDao,
        @ApplicationContext applicationContext: Context
    ) : AddMedicineInteractor {
        return AddMedicineInteractor(
            service = service,
            cache = localMedicineDao,
            applicationContext
        )
    }
}