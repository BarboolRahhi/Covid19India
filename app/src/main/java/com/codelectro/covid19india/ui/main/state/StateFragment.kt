package com.codelectro.covid19india.ui.main.state

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.codelectro.covid19india.R
import com.codelectro.covid19india.models.State
import com.codelectro.covid19india.models.Total
import com.codelectro.covid19india.ui.main.MainActivity
import com.codelectro.covid19india.util.ApiResult
import com.codelectro.covid19india.util.VerticalSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_state.*
import javax.inject.Inject

class StateFragment : Fragment() {

    companion object {
        private const val TAG = "StateFragment"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var adapter: StateRecyclerAdapter

    lateinit var viewModel: StateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_state, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(StateViewModel::class.java)

//        viewModel.getDistricts("IN-MH").observe(viewLifecycleOwner, Observer {
//            execute(it)
//        })


        initRecycleView()

        viewModel.getStates().observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onViewCreated: ")
                execute(it)
        })


    }

    private fun execute(apiResult: ApiResult<List<State>?>) {
        when (apiResult) {
            is ApiResult.Success -> { apiResult.value?.let { adapter.setStateList(it) } }
            is ApiResult.GenericError -> Log.d(MainActivity.TAG, "onCreate: ${apiResult.errorMessage}")
            ApiResult.NetworkError -> Log.d(MainActivity.TAG, "onCreate: NetworkError")
        }
    }

    private fun initRecycleView() {
        recycleView.layoutManager = linearLayoutManager
        recycleView.addItemDecoration(VerticalSpacingItemDecoration(30))
        recycleView.adapter = adapter
    }

}