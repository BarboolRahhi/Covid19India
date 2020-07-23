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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.codelectro.covid19india.R
import com.codelectro.covid19india.models.State
import com.codelectro.covid19india.ui.main.MainActivity
import com.codelectro.covid19india.ui.showToast
import com.codelectro.covid19india.util.ApiResult
import com.codelectro.covid19india.util.VerticalSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_state.*
import javax.inject.Inject
import javax.inject.Provider

class StateFragment : Fragment() {

    companion object {
        private const val TAG = "StateFragment"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var linearLayoutManager: Provider<RecyclerView.LayoutManager>

    @Inject
    lateinit var adapter: StateRecyclerAdapter

    lateinit var viewModel: StateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

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
        Log.d(TAG, "onViewCreated: $viewModel")

        val navController = Navigation.findNavController(view)
        
        initRecycleView()


        showProgressBar(true)

        viewModel.state.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onViewCreated: ")
                execute(it)
        })

        refresh.setOnClickListener {
            viewModel.refreshStatesData()
            showProgressBar(true)
            showRefreshBtn(false)
        }
        
        adapter.setOnClickItemListener(object : StateRecyclerAdapter.ClickListener {
            override fun onClick(view: View, state: State) {
                val action = StateFragmentDirections.actionStateToDistrictFragment(state.id)
                navController.navigate(action)
            }
        })

    }

    private fun execute(apiResult: ApiResult<List<State>?>) {
        when (apiResult) {
            is ApiResult.Success -> {
                apiResult.value?.let { adapter.setStateList(it) }
                refresh.visibility = View.INVISIBLE
                showProgressBar(false)
                showRefreshBtn(false)
            }
            is ApiResult.GenericError -> {
                showProgressBar(false)
                showRefreshBtn(false)
                if (apiResult.code == 408) {
                    showRefreshBtn(true)
                    requireContext().showToast("Network Timeout! Please check your internet connection.")
                }
                Log.d(MainActivity.TAG, "onCreate: ${apiResult.errorMessage}")
            }
            ApiResult.NetworkError -> {
                showProgressBar(false)
                showRefreshBtn(true)
                requireContext().showToast("Network Error! Please check your internet connection.")
                Log.d(MainActivity.TAG, "onCreate: NetworkError")
            }
        }
    }

    private fun initRecycleView() {
        recycleView.layoutManager = linearLayoutManager.get()
        recycleView.addItemDecoration(VerticalSpacingItemDecoration(30))
        recycleView.adapter = adapter
    }


    private fun showProgressBar(isVisible: Boolean) {
        if (isVisible)
            progress_bar.visibility = View.VISIBLE
        else
            progress_bar.visibility = View.INVISIBLE
    }

    private fun showRefreshBtn(isVisible: Boolean) {
        if (isVisible)
            refresh.visibility = View.VISIBLE
        else
            refresh.visibility = View.INVISIBLE
    }

}