package com.codelectro.covid19india.entity

data class DistrictData(
    val districtData: HashMap<String, DistrictNetwork>? = null,
    val statecode: String
)