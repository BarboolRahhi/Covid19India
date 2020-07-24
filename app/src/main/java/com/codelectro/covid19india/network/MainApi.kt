package com.codelectro.covid19india.network

import com.codelectro.covid19india.entity.CovidData
import com.codelectro.covid19india.entity.States
import retrofit2.Response
import retrofit2.http.GET


interface MainApi {

    @GET("data.json")
    suspend fun getCovidData(): Response<CovidData>

    @GET("state_district_wise.json")
    suspend fun getDistrictData(): Response<States>

}