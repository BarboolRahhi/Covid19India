package com.codelectro.covid19india.network

import com.codelectro.covid19india.models.GraphData
import retrofit2.http.GET

interface GraphApi {

    @GET("india")
    suspend fun getGraphData(): List<GraphData>
}