package com.codelectro.covid19india.repository

import com.codelectro.covid19india.db.MainDao
import com.codelectro.covid19india.entity.CasesSeries
import com.codelectro.covid19india.entity.District
import com.codelectro.covid19india.entity.StateWise
import com.codelectro.covid19india.network.MainApi
import com.codelectro.covid19india.util.CaseSort
import com.codelectro.covid19india.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class MainRepository constructor(
    private val mainApi: MainApi,
    private val mainDao: MainDao
) {

    fun setCovidData(): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            val response = mainApi.getCovidData()
            Timber.d("Data: Fetching Data...")
            if (response.isSuccessful) {
                response.body()?.let {
                    mainDao.deleteAllCasesSeries()
                    mainDao.insertAllStateWise(it.statewise)
                    mainDao.insertAllCasesSeries(it.cases_time_series)
                    emit(DataState.Success(true))
                }
            }
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    fun setDistrictData(): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            val response = mainApi.getDistrictData()
            if (response.isSuccessful) {
                response.body()?.let {
                    val list = ArrayList<District>()
                    val map = response.body()!!.map
                    for (stateEntry in map) {
                        for (districtEntry in stateEntry.value.districtData!!) {
                            list.add(
                                District(
                                    keyId = "${districtEntry.key}${stateEntry.value.statecode}",
                                    statecode = stateEntry.value.statecode,
                                    name = districtEntry.key,
                                    active = districtEntry.value.active,
                                    confirmed = districtEntry.value.confirmed,
                                    deceased = districtEntry.value.deceased,
                                    recovered = districtEntry.value.recovered,
                                    deltaconfirmed = districtEntry.value.delta.confirmed,
                                    deltarecovered = districtEntry.value.delta.recovered,
                                    deltadeaths = districtEntry.value.delta.deceased
                                )
                            )
                        }
                    }
                    mainDao.insertAllDistricts(list)
                    emit(DataState.Success(true))
                }
            }
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    fun getCasesSeriesData(limit: Int): Flow<List<CasesSeries>> = flow {
        if (limit == -1)
            emit(mainDao.getAllCasesSeries())
        else
            emit(mainDao.getAllCasesSeries().takeLast(limit))
    }

    fun getStateWiseData(caseSort: CaseSort): Flow<List<StateWise>> = flow {
        when (caseSort) {
            CaseSort.CONFIRMED -> emit(mainDao.getAllStateWiseByConfirmed())
            CaseSort.ACTIVE ->  emit(mainDao.getAllStateWiseByActive())
            CaseSort.RECOVERED ->  emit(mainDao.getAllStateWiseByRecovered())
            CaseSort.DEATHS ->  emit(mainDao.getAllStateWiseByDeaths())
        }
    }

    fun getTotalData() = mainDao.getTotalData("TT")
    fun getDistrictByStateCode(code: String, caseSort: CaseSort): Flow<List<District>> = flow {
        when (caseSort) {
            CaseSort.CONFIRMED -> emit(mainDao.getDistrictByStateCodeAndConfirmed(code))
            CaseSort.ACTIVE ->  emit(mainDao.getDistrictByStateCodeAndActive(code))
            CaseSort.RECOVERED ->  emit(mainDao.getDistrictByStateCodeAndRecovered(code))
            CaseSort.DEATHS ->  emit(mainDao.getDistrictByStateCodeAndDeaths(code))
        }
    }

}