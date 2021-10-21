package com.devscore.digital_pharmacy.presentation

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.work.*
import com.devscore.digital_pharmacy.business.datasource.cache.AppDatabase
import com.devscore.digital_pharmacy.presentation.util.SyncWorker
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidApp
class BaseApplication : Application(), Configuration.Provider{

//    lateinit var database: AppDatabase

    val TAG = "BaseApplication"


    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()


    override fun onCreate() {
        super.onCreate()


//        database = Room
//            .databaseBuilder(this, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
//            .fallbackToDestructiveMigration() // get correct db version if schema changed
//            .build()
        syncEngine()
    }

    private fun syncEngine() {
//        CoroutineScope(IO).launch {
//        }

//        database.getLocalMedicineDao().getRequestedData(-1).observeForever( Observer { data ->
//            if (data.size > 0){
//                Log.d(TAG, data.toString())
//            }
//        })



        Log.d(TAG, "application")

        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        /*val syncWorkRequest : WorkRequest =
            PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

        WorkManager
            .getInstance(this)
            .enqueue(syncWorkRequest)*/

    }
}