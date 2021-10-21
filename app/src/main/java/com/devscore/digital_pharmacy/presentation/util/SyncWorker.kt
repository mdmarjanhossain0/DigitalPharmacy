package com.devscore.digital_pharmacy.presentation.util

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.devscore.digital_pharmacy.business.datasource.cache.AppDatabase
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.LocalMedicineDao
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.toLocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.toAddMedicine
import com.devscore.digital_pharmacy.business.interactors.inventory.AddMedicineInteractor
import com.devscore.digital_pharmacy.presentation.session.SessionManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@HiltWorker
class SyncWorker @AssistedInject
constructor(
    @Assisted
    context: Context,
    @Assisted
    parameters: WorkerParameters,
    private val localMedicineDao : AppDatabase,
    private var addMedicineInteractor: AddMedicineInteractor,
    var sessionManager : SessionManager
) : Worker(context, parameters) {

    val TAG = "SyncWorker"

    override fun doWork(): Result {

//        sync()
        return Result.success()
    }

    private fun sync() {
        Log.d(TAG, "Enter successful")
        val localMedicines = localMedicineDao.getLocalMedicineDao().getSyncData().map {
            it.toLocalMedicine()
        }
        val addMedicines = localMedicines.map {
            it.toAddMedicine()
        }



        Log.d(TAG, addMedicines.size.toString())
        Log.d(TAG, addMedicines.toString())
        Log.d(TAG, addMedicineInteractor.toString())


        for (addMedicine in addMedicines) {
            Log.d(TAG, "Sync Worker " + addMedicine.toString())
            addMedicineInteractor.execute(
                authToken = sessionManager.state.value?.authToken,
                addMedicine
            )
        }
    }
}