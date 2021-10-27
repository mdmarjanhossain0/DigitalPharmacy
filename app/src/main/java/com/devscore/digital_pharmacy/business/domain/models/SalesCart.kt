package com.devscore.digital_pharmacy.business.domain.models

data class SalesCart (
    var medicine : LocalMedicine?,
    var orderMedicine : SalesOrderMedicine,
    var salesUnit : MedicineUnits?,
    var amount : Float?
        )