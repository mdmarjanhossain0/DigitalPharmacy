package com.devscore.digital_pharmacy.presentation.util

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.devscore.digital_pharmacy.business.datasource.cache.AppDatabase
import com.devscore.digital_pharmacy.business.datasource.cache.account.AccountDao
import com.devscore.digital_pharmacy.business.datasource.cache.auth.AuthTokenDao
import com.devscore.digital_pharmacy.business.datasource.cache.auth.toAuthToken
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.toFailureMedicine
import com.devscore.digital_pharmacy.business.datasource.cache.inventory.local.toLocalMedicine
import com.devscore.digital_pharmacy.business.datasource.datastore.AppDataStore
import com.devscore.digital_pharmacy.business.datasource.network.inventory.InventoryApiService
import com.devscore.digital_pharmacy.business.datasource.network.inventory.network_responses.toLocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.AuthToken
import com.devscore.digital_pharmacy.business.domain.models.toAddMedicine
import com.devscore.digital_pharmacy.business.domain.models.toLocalMedicine
import com.devscore.digital_pharmacy.business.domain.util.DataState
import com.devscore.digital_pharmacy.business.domain.util.StateMessageCallback
import com.devscore.digital_pharmacy.business.interactors.inventory.AddMedicineInteractor
import com.devscore.digital_pharmacy.presentation.session.SessionEvents
import com.devscore.digital_pharmacy.presentation.session.SessionManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@HiltWorker
class UploadWorker @AssistedInject
constructor(
    @Assisted
    context: Context,
    @Assisted
    parameters: WorkerParameters,
    private val database : AppDatabase,
    private var addMedicineInteractor: AddMedicineInteractor,
    var sessionManager : SessionManager,
    val service : InventoryApiService,
    val appDataStoreManager : AppDataStore,
    val accountDao: AccountDao,
    val authTokenDao: AuthTokenDao
) : Worker(context, parameters) {

    val TAG = "UploadWorker"

    override fun doWork(): Result {

        sync()
        return Result.success()
    }

    private fun sync() {
        Log.d(TAG, "Enter successful")
        val localMedicines = database.getLocalMedicineDao().getSyncData().map {
            it.toLocalMedicine()
        }
        val addMedicines = localMedicines.map {
            it.toAddMedicine()
        }



        Log.d(TAG, addMedicines.size.toString())
        Log.d(TAG, addMedicines.toString())
        Log.d(TAG, addMedicineInteractor.toString())

        Log.d(TAG, "SessionManager " + sessionManager.toString())
        Log.d(TAG, "Token " + sessionManager.state.value?.authToken?.token.toString())

        CoroutineScope(Main).launch {
            Log.d(TAG, Thread.currentThread().name.toString())
            appDataStoreManager.readValue(DataStoreKeys.PREVIOUS_AUTH_USER)?.let { email ->
                var authToken: AuthToken? = null
                val entity = accountDao.searchByEmail(email)
                if(entity != null){
                    authToken = authTokenDao.searchByPk(entity.pk)?.toAuthToken()
                    if(authToken != null){
                        for (addMedicine in addMedicines) {
                            try {
                                val medicineResponse = service.addMedicine(
                                    "Token ${authToken.token}",
                                    medicine = addMedicine
                                ).toLocalMedicine()
                                database.getLocalMedicineDao().deleteFailureMedicine(addMedicine.toLocalMedicine().toFailureMedicine())
                            }
                            catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }


        }



    }
}