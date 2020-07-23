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
import androidx.recyclerview.widget.RecyclerView
import com.codelectro.covid19india.R
import com.codelectro.covid19india.models.District
import com.codelectro.covid19india.ui.main.MainActivity
import com.codelectro.covid19india.ui.showToast
import com.codelectro.covid19india.util.ApiResult
import com.codelectro.covid19india.util.VerticalSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_district.*
import kotlinx.android.synthetic.main.fragment_district.progress_bar
import kotlinx.android.synthetic.main.fragment_district.recycleView
import kotlinx.android.synthetic.main.fragment_district.refresh
import javax.inject.Inject
import javax.inject.Provider

class DistrictFragment : Fragment() {

    companion object {
        private const val TAG = "StateFragment"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var linearLayoutManager: Provider<RecyclerView.LayoutManager>

    @Inject
    lateinit var adapter: DistrictRecyclerAdapter

    private lateinit var viewModel: DistrictViewModel

    lateinit var name: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_district, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val args = DistrictFragmentArgs.fromBundle(requireArguments())
            name = args.name
            Log.d(TAG, "onViewCreated: $name")
        }

        viewModel = ViewModelProvider(this, viewModelFactory).get(DistrictViewModel::class.java)

        initRecycleView()

        showProgressBar(true)
        observeSubscriber()

        refresh.setOnClickListener {
            viewModel.refreshDistrictsData()
            showProgressBar(true)
            showRefreshBtn(false)
        }

    }

    private fun observeSubscriber() {
        viewModel.setStateCode(name)
        viewModel.refreshDistrictsData()
        viewModel.districts.removeObservers(viewLifecycleOwner)
        viewModel.districts.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onViewCreated: ")
            execute(it)
        })
    }

    private fun execute(apiResult: ApiResult<List<District>?>) {
        when (apiResult) {
            is ApiResult.Success -> {
                apiResult.value?.let { adapter.setStateList(it) }
                Log.d(TAG, "execute: ${apiResult.value}")
                showProgressBar(false)
                showRefreshBtn(false)
                apiResult.value?.let {
                    if (it.isEmpty())
                        showNoDataFound(true)
                }

            }
            is ApiResult.GenericError -> {
                showProgressBar(false)
                showRefreshBtn(false)
                Log.d(MainActivity.TAG, "onCreate: ${apiResult.errorMessage}")
            }
            ApiResult.NetworkError -> {
                showProgressBar(false)
                showRefreshBtn(true)
                requireActivity().showToast("Network Problem!")
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

    private fun showNoDataFound(isVisible: Boolean) {
        if (isVisible)
            no_data_found.visibility = View.VISIBLE
        else
            no_data_found.visibility = View.INVISIBLE
    }

    private fun showRefreshBtn(isVisible: Boolean) {
        if (isVisible)
            refresh.visibility = View.VISIBLE
        else
            refresh.visibility = View.INVISIBLE
    }

}