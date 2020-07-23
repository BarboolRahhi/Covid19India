package com.codelectro.covid19india.ui.main.state

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.codelectro.covid19india.models.District
import com.codelectro.covid19india.models.State
import com.codelectro.covid19india.repository.MainRepository
import com.codelectro.covid19india.util.ApiResult
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DistrictViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    companion object {
        private const val TAG = "DistrictViewModel"
    }

    private var _districts = MutableLiveData<ApiResult<List<District>?>>()
    val districts: LiveData<ApiResult<List<District>?>>
        get() = _districts

    private var stateCode: String = ""

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)


    fun setStateCode(code: String) {
        stateCode = code
    }

    private fun getDistricts() {
        Log.d(TAG, "getDistricts: called")
        if (stateCode.isNotEmpty())
            scope.launch {
                _districts.value = repository.getDistricts(stateCode)
            }
    }


    fun refreshDistrictsData() {
        getDistricts()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: DistrictViewModel")
        _districts.value = ApiResult.Success(null)
        coroutineContext.cancel()
    }
}