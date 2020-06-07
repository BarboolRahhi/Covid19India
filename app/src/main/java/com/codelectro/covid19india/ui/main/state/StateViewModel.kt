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

class StateViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    companion object {
        private const val TAG = "TotalViewModel"
    }

    private var stateName = MutableLiveData<String>()
    private var _districts = MutableLiveData<ApiResult<List<District>?>>()
    private var _state = MutableLiveData<ApiResult<List<State>?>>()
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    init {
        Log.d(TAG, "OnViewModelInit: working...")
        scope.launch {
            val apiResult = repository.getStates()
            _state.value = apiResult
        }

    }


    fun getDistricts(stateId: String): LiveData<ApiResult<List<District>?>> {
        scope.launch {
            val apiResult = repository.getDistricts(stateId)
            _districts.value = apiResult
        }
        return _districts
    }

    fun getStates(): LiveData<ApiResult<List<State>?>> = _state

    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancel()
    }
}