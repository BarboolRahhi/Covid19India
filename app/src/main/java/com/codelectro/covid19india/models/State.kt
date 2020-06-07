package com.codelectro.covid19india.models

import com.google.gson.annotations.SerializedName


data class State(
    @SerializedName("active")
    val active: Int,
    @SerializedName("confirmed")
    val confirmed: Int,
    @SerializedName("deaths")
    val deaths: Int,
    @SerializedName("districtData")
    val districts: List<District>,
    @SerializedName("id")
    val id: String,
    @SerializedName("recovered")
    val recovered: Int,
    @SerializedName("state")
    val state: String
)

data class District(
    @SerializedName("confirmed")
    val confirmed: Int,
    @SerializedName("deaths")
    val deaths: Any,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("recovered")
    val recovered: Any,
    @SerializedName("zone")
    val zone: String
)
