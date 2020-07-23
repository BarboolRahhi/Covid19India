package com.codelectro.covid19india.models

import com.anychart.chart.common.dataentry.ValueDataEntry
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
): ValueDataEntry(x, value) {

    init {
        setValue("value2", value2)
        setValue("value3", value3)
    }
}