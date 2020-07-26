package com.codelectro.covid19india.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codelectro.covid19india.entity.CasesSeries
import com.codelectro.covid19india.entity.District
import com.codelectro.covid19india.entity.StateWise
import com.codelectro.covid19india.repository.MainRepository
import com.codelectro.covid19india.util.CaseSort
import com.codelectro.covid19india.util.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Singleton

@Singleton
class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository
) : ViewModel() {

    var checkedItem = 0

    private val _covidDataSource: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val covidDataState: LiveData<DataState<Boolean>>
        get() = _covidDataSource

    private val _districtDataSource: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val districtDataState: LiveData<DataState<Boolean>>
        get() = _districtDataSource

    private val _caseseries: MutableLiveData<List<CasesSeries>> = MutableLiveData()
    val casesSeries: LiveData<List<CasesSeries>>
        get() = _caseseries

    private val _stateWise: MutableLiveData<List<StateWise>> = MutableLiveData()
    val stateWise: LiveData<List<StateWise>>
        get() = _stateWise

    private val _districts: MutableLiveData<List<District>> = MutableLiveData()
    val districts: LiveData<List<District>>
        get() = _districts

    init {
        setCovidData()
        setDistrictData()
        getStateWiseData(CaseSort.CONFIRMED)
    }

    private fun setCovidData() {
        viewModelScope.launch {
            repository.setCovidData()
                .onEach { dataState ->
                    _covidDataSource.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }

    private fun setDistrictData() {
        viewModelScope.launch {
            repository.setDistrictData()
                .onEach { dataState ->
                    _districtDataSource.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun getCasesSeriesData(limit: Int)  {
        viewModelScope.launch {
            repository.getCasesSeriesData(limit)
                .onEach {
                    _caseseries.value = it
                }.launchIn(viewModelScope)
        }
    }

    fun getStateWiseData(caseSort: CaseSort) {
        viewModelScope.launch {
            repository.getStateWiseData(caseSort)
                .onEach {
                    _stateWise.value = it
                }.launchIn(viewModelScope)
        }
    }

    fun getDistrictByStateCode(code: String, caseSort: CaseSort) {
        viewModelScope.launch {
            repository.getDistrictByStateCode(code, caseSort)
                .onEach {
                    _districts.value = it
                }.launchIn(viewModelScope)
        }
    }

    fun getTotalData() = repository.getTotalData()

}