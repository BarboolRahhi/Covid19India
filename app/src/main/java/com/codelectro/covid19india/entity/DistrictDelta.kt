package com.codelectro.covid19india.entity

data class DistrictDelta(
    val confirmed: Int = 0,
    val deceased: Int = 0,
    val recovered: Int = 0
)