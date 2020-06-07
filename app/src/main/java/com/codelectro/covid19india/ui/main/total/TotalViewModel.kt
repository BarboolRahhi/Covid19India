package com.codelectro.covid19india.ui.main.total

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codelectro.covid19india.models.Total
import com.codelectro.covid19india.repository.MainRepository
import com.codelectro.covid19india.util.ApiResult
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TotalViewModel @Inject constructor(private val repository: MainRepository) : ViewModel()  {

    companion object {
        private const val TAG = "TotalViewModel"
    }

    private var _summarry  = MutableLiveData<ApiResult<Total?>>()
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    init {
        Log.d(TAG, "OnViewModelInit: working...")
        scope.launch {
            val apiResult = repository.getSummary()
            _summarry.value = apiResult
        }
    }

    fun getSummary(): LiveData<ApiResult<Total?>> = _summarry

    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancel()
    }
}