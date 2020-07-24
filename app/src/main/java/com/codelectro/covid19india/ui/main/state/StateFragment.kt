package com.codelectro.covid19india.ui.main.state

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.codelectro.covid19india.R
import com.codelectro.covid19india.entity.StateWise
import com.codelectro.covid19india.exception.NoInternetException
import com.codelectro.covid19india.ui.MainViewModel
import com.codelectro.covid19india.ui.main.MainActivity
import com.codelectro.covid19india.ui.showToast
import com.codelectro.covid19india.util.DataState
import com.codelectro.covid19india.util.VerticalSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_state.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class StateFragment : Fragment(R.layout.fragment_state) {

    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var adapter: StateRecyclerAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)
        viewModel = (activity as MainActivity).viewModel
        initRecycleView()

        viewModel.covidDataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    Timber.d("Data: ${dataState.data}")
                    subscribeObserver()
                    showProgressBar(false)
                }
                is DataState.Error -> {
                    Timber.d("Data: ${dataState.exception.message}")
                    when(dataState.exception) {
                        is NoInternetException -> {
                            dataState.exception.message?.let { requireContext().showToast(it) }
                        }
                    }
                    subscribeObserver()
                    showProgressBar(false)
                }
                DataState.Loading ->{
                    showProgressBar(true)
                    Timber.d("Data: Loading")
                }
            }
        })

        adapter.setOnClickItemListener(object : StateRecyclerAdapter.ClickListener {
            override fun onClick(view: View, state: StateWise) {
                val action = StateFragmentDirections.actionStateToDistrictFragment(state.statecode)
                navController.navigate(action)
            }
        })

    }

    private fun subscribeObserver() {
        viewModel.getStateWiseData().observe(viewLifecycleOwner, Observer {
            adapter.setStateList(it)
        })
    }


    private fun initRecycleView() {
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        recycleView.addItemDecoration(VerticalSpacingItemDecoration(30))
        recycleView.adapter = adapter
    }


    private fun showProgressBar(isVisible: Boolean) {
        progress_bar.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

}