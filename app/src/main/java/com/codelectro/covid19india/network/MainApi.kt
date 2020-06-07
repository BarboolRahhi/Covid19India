package com.codelectro.covid19india.network

import com.codelectro.covid19india.models.State
import com.codelectro.covid19india.models.Total
import retrofit2.http.GET


interface MainApi {

    @GET("total.json")
    suspend fun getSummary(): Total

    @GET("state_data.json")
    suspend fun getStates(): List<State>

}