package com.devscore.digital_pharmacy.business.domain.models

import com.devscore.digital_pharmacy.business.datasource.cache.sales.SalesOderMedicineEntity
import com.google.gson.annotations.SerializedName

data class SalesOderMedicine (

    var pk : Int? = -1,
    var room_id : Long? = -1,
    var unit : Int,
    var quantity : Long,
    var local_medicine : Int,
    var brand_name : String? = null

)