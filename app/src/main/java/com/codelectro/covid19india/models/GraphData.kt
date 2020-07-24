package com.codelectro.covid19india.models


import com.google.gson.annotations.SerializedName

data class GraphData internal constructor(
    @SerializedName("Date")
    val x: String?,
    @SerializedName("Confirmed")
    val value: Int?,
    @SerializedName("Deaths")
    val value2: Int?,
    @SerializedName("Recovered")
    val value3: Int?
)