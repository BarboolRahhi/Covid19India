package com.codelectro.covid19india.models

import com.google.gson.annotations.SerializedName


data class Total(
    @SerializedName("aChanges")
    val aChanges: Int,
    @SerializedName("achanges")
    val achanges: Int,
    @SerializedName("active")
    val active: Int,
    @SerializedName("cChanges")
    val cChanges: Int,
    @SerializedName("cchanges")
    val cchanges: Int,
    @SerializedName("confirmed")
    val confirmed: Int,
    @SerializedName("dChanges")
    val dChanges: Int,
    @SerializedName("dchanges")
    val dchanges: Int,
    @SerializedName("deaths")
    val deaths: Int,
    @SerializedName("districtData")
    val districtData: Any,
    @SerializedName("id")
    val id: String,
    @SerializedName("rChanges")
    val rChanges: Int,
    @SerializedName("rchanges")
    val rchanges: Int,
    @SerializedName("recovered")
    val recovered: Int,
    @SerializedName("state")
    val state: String
) {
    override fun toString(): String {
        return "Total(active=$active, confirmed=$confirmed, deaths=$deaths, recovered=$recovered)"
    }
}