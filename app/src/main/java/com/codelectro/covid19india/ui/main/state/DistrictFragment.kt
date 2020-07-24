package com.codelectro.covid19india.ui.main.state

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.codelectro.covid19india.R
import com.codelectro.covid19india.exception.NoInternetException
import com.codelectro.covid19india.ui.MainViewModel
import com.codelectro.covid19india.ui.main.MainActivity
import com.codelectro.covid19india.ui.showToast
import com.codelectro.covid19india.util.DataState
import com.codelectro.covid19india.util.VerticalSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_district.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DistrictFragment : Fragment(R.layout.fragment_district) {

    companion object {
        private const val TAG = "StateFragment"
    }

    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var adapter: DistrictRecyclerAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        initRecycleView()

        arguments?.let {
            val args = DistrictFragmentArgs.fromBundle(requireArguments())
            observeSubscriber(args.name)
        }

        viewModel.districtDataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    Timber.d("Data: ${dataState.data}")
                    showProgressBar(false)
                }
                is DataState.Error -> {
                    Timber.d("Data: ${dataState.exception.message}")
                    when(dataState.exception) {
                        is NoInternetException -> {
                            dataState.exception.message?.let { requireContext().showToast(it) }
                        }
                    }
                    showProgressBar(false)
                }
                DataState.Loading -> {
                    Timber.d("Data: Loading")
                    showProgressBar(true)
                }
            }
        })


    }

    private fun observeSubscriber(stateCode: String) {

        viewModel.getDistrictByStateCode(stateCode).observe(requireActivity(), Observer {
            Timber.d("district: $it")
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