package com.devscore.digital_pharmacy.business.datasource.network.auth.network_responses

import com.google.gson.annotations.SerializedName

class RegistrationResponse(

    @SerializedName("response")
    var response: String,

    @SerializedName("error_message")
    var errorMessage: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("username")
    var username: String,

    @SerializedName("pk")
    var pk: Int,

    @SerializedName("token")
    var token: String,

    @SerializedName("profile_picture")
    var profile_picture : String?,

//    @SerializedName("business_name")
//    var business_name : String,

    @SerializedName("mobile")
    var mobile : String,

    @SerializedName("license_key")
    var license_key : String,

    @SerializedName("address")
    var address : String
)