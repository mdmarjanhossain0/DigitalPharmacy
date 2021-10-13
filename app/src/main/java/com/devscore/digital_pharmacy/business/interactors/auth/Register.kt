package com.devscore.digital_pharmacy.business.interactors.auth

import com.devscore.digital_pharmacy.business.datasource.cache.account.AccountDao
import com.devscore.digital_pharmacy.business.datasource.cache.account.toEntity
import com.devscore.digital_pharmacy.business.datasource.cache.auth.AuthTokenDao
import com.devscore.digital_pharmacy.business.datasource.cache.auth.toEntity
import com.devscore.digital_pharmacy.business.datasource.datastore.AppDataStore
import com.devscore.digital_pharmacy.business.datasource.network.auth.AuthService
import com.devscore.digital_pharmacy.business.datasource.network.handleUseCaseException
import com.devscore.digital_pharmacy.business.domain.models.Account
import com.devscore.digital_pharmacy.business.domain.models.AuthToken
import com.devscore.digital_pharmacy.business.domain.util.DataState
import com.devscore.digital_pharmacy.business.domain.util.ErrorHandling
import com.devscore.digital_pharmacy.business.domain.util.ErrorHandling.Companion.ERROR_SAVE_AUTH_TOKEN
import com.devscore.digital_pharmacy.presentation.util.DataStoreKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class Register(
    private val service: AuthService,
    private val accountDao: AccountDao,
    private val authTokenDao: AuthTokenDao,
    private val appDataStoreManager: AppDataStore,
){
    fun execute(
        email: String,
        username: String,
        password: String,
        confirmPassword: String,
        businessName : String,
        mobile : String,
        license_key : String,
        address : String
    ): Flow<DataState<AuthToken>> = flow {
        emit(DataState.loading<AuthToken>())
        val registerResponse = service.register(
            email = email,
            username = username,
            password = password,
            password2 = confirmPassword,
            business_name = businessName,
            mobile = mobile,
            license_key = license_key,
            address = address
        )
        // Incorrect login credentials counts as a 200 response from server, so need to handle that
        if(registerResponse.response.equals(ErrorHandling.GENERIC_AUTH_ERROR)){
            throw Exception(registerResponse.errorMessage)
        }

        // cache account information
        accountDao.insertAndReplace(
            Account(
                registerResponse.pk,
                registerResponse.email,
                registerResponse.username,
                registerResponse.profile_picture!!,
                registerResponse.business_name,
                registerResponse.mobile,
                registerResponse.license_key,
                registerResponse.address
            ).toEntity()
        )

        // cache the auth token
        val authToken = AuthToken(
            registerResponse.pk,
            registerResponse.token
        )
        val result = authTokenDao.insert(authToken.toEntity())
        // can't proceed unless token can be cached
        if(result < 0){
            throw Exception(ERROR_SAVE_AUTH_TOKEN)
        }
        // save authenticated user to datastore for auto-login next time
        appDataStoreManager.setValue(DataStoreKeys.PREVIOUS_AUTH_USER, email)
        emit(DataState.data(data = authToken, response = null))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}














