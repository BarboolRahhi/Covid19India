package com.codelectro.covid19india.repository

import com.codelectro.covid19india.di.main.ActivityScope
import com.codelectro.covid19india.models.*
import com.codelectro.covid19india.network.GraphApi
import com.codelectro.covid19india.network.MainApi
import com.codelectro.covid19india.util.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import java.util.stream.Collector
import java.util.stream.Collectors
import javax.inject.Inject
import javax.inject.Singleton


@ActivityScope
class MainRepository @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val mainApi: MainApi,
    private val graphApi: GraphApi
) {

    suspend fun getSummary() : ApiResult<Total?> {
        return safeApiCall(dispatcher) {
            mainApi.getSummary()
        }
    }

    suspend fun getStates() : ApiResult<List<State>?> {
        return safeApiCall(dispatcher) {
            mainApi.getStates()
        }
    }

    suspend fun getDistricts(stateId: String) : ApiResult<List<District>?> {
        return safeApiCall(dispatcher) {
            mainApi.getStates()
                .filter { state -> state.id == stateId }
                .flatMap { state ->  state.districts }
                .toList()
        }
    }

    suspend fun getGraphData(): ApiResult<List<GraphData>?> {
        return safeApiCall(dispatcher) {
            graphApi.getGraphData()
        }
    }
}