package com.codelectro.covid19india.entity

data class CovidData(
    val cases_time_series: List<CasesSeries>,
    val statewise: List<StateWise>
)