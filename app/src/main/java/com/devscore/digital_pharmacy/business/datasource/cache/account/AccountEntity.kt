package com.devscore.digital_pharmacy.business.datasource.cache.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devscore.digital_pharmacy.business.domain.models.Account


@Entity(tableName = "account_properties")
data class AccountEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pk")
    val pk: Int,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "business_name")
    val business_name : String,

    @ColumnInfo(name = "mobile")
    val mobile : String,

    @ColumnInfo(name = "license_key")
    val license_key : String,

    @ColumnInfo(name = "address")
    val address : String,

    @ColumnInfo(name = "profile_picture")
    val profile_picture : String?
)

fun AccountEntity.toAccount(): Account {
    return Account(
        pk = pk,
        email = email,
        username = username,
        profile_picture = profile_picture!!,
        business_name = business_name,
        mobile = mobile,
        license_key = license_key,
        address = address
    )
}

fun Account.toEntity(): AccountEntity {
    return AccountEntity(
        pk = pk,
        email = email,
        username = username,
        profile_picture = profile_picture,
        business_name = business_name,
        mobile = mobile,
        license_key = license_key,
        address = address
    )
}