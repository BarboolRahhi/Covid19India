package com.codelectro.covid19india.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codelectro.covid19india.repository.MainRepository
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

    private val _covidDataSource: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val covidDataState: LiveData<DataState<Boolean>>
        get() = _covidDataSource

    private val _districtDataSource: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val districtDataState: LiveData<DataState<Boolean>>
        get() = _districtDataSource

    init {
        setCovidData()
        setDistrictData()
    }

    fun setCovidData() {
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
                }
                .launchIn(viewModelScope)
        }
    }

    fun getCasesSeriesData() = repository.getCasesSeriesData()
    fun getStateWiseData() = repository.getStateWiseData()
    fun getTotalData() = repository.getTotalData()
    fun getDistrictByStateCode(code: String) = repository.getDistrictByStateCode(code)
}