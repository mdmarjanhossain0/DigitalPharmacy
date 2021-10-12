package com.devscore.digital_pharmacy.business.datasource.network.auth

import com.devscore.digital_pharmacy.business.datasource.network.auth.network_responses.LoginResponse
import com.devscore.digital_pharmacy.business.datasource.network.auth.network_responses.RegistrationResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface AuthService {

    @POST("account/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("account/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("email") email: String = "abc1@gmail.com",
        @Field("username") username: String = "a",
        @Field("password") password: String = "adminpassword",
        @Field("password2") password2: String = "adminpassword",
        @Field("business_name") business_name: String = "a",
        @Field("mobile") mobile: String = "54354",
        @Field("license_key") license_key: String = "45345",
        @Field("address") address: String = "BD"
    ): RegistrationResponse

}
