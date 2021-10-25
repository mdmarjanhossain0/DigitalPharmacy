package com.devscore.digital_pharmacy.business.datasource.cache.sales

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "SalesOrder")
data class SalesOrderEntity (

    @ColumnInfo(name = "pk")
    @PrimaryKey(autoGenerate = false)
    var pk : Int?,

    @ColumnInfo(name = "customer")
    var customer : Int?,

    @ColumnInfo(name = "total_amount")
    var total_amount : Long?,

    @ColumnInfo(name = "total_after_discount")
    var total_after_discount : Long?,

    @ColumnInfo(name = "paid_amount")
    var paid_amount : Long?,

    @ColumnInfo(name = "discount")
    var discount : Long?,

    @ColumnInfo(name = "is_discount_percent")
    var is_discount_percent : Boolean,

    @ColumnInfo(name = "created_at")
    var created_at : String,

    @ColumnInfo(name = "updated_at")
    var updated_at : String?
)