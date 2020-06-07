package com.codelectro.covid19india.repository

import com.codelectro.covid19india.models.District
import com.codelectro.covid19india.models.State
import com.codelectro.covid19india.models.Summary
import com.codelectro.covid19india.models.Total
import com.codelectro.covid19india.network.MainApi
import com.codelectro.covid19india.util.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import java.util.stream.Collector
import java.util.stream.Collectors
import javax.inject.Inject

class MainRepository @Inject constructor(val dispatcher: CoroutineDispatcher, val mainApi: MainApi) {

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
}