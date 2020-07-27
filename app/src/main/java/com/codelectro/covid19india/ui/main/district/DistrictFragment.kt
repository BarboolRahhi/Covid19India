package com.codelectro.covid19india.ui.main.district

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.codelectro.covid19india.R
import com.codelectro.covid19india.adapters.DistrictRecyclerAdapter
import com.codelectro.covid19india.exception.NoInternetException
import com.codelectro.covid19india.ui.MainViewModel
import com.codelectro.covid19india.ui.main.MainActivity
import com.codelectro.covid19india.ui.showToast
import com.codelectro.covid19india.util.CaseSort
import com.codelectro.covid19india.util.DataState
import com.codelectro.covid19india.util.VerticalSpacingItemDecoration
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
    private var menu: Menu? = null

    var stateCode: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        initRecycleView()

        arguments?.let {
            val args =
                DistrictFragmentArgs.fromBundle(
                    requireArguments()
                )
            stateCode = args.name
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

        observeSubscriber()
    }

    private fun observeSubscriber() {
        viewModel.getDistrictByStateCode(stateCode, CaseSort.CONFIRMED)
        viewModel.districts.observe(requireActivity(), Observer {
            Timber.d("district: $it")
            adapter.setStateList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
        menu.getItem(0)?.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.sort -> {
                showSortDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSortDialog() {
        val items = arrayOf("Confirmed", "Active", "Recovered", "Deaths")
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Sort cases by:")
            .setSingleChoiceItems(items, viewModel.districtCheckedItem) { dialog, which ->
                when(which) {
                    0 -> {
                        viewModel.getDistrictByStateCode(stateCode, CaseSort.CONFIRMED)
                        viewModel.districtCheckedItem = 0
                        dialog.cancel()
                    }
                    1 -> {
                        viewModel.getDistrictByStateCode(stateCode, CaseSort.ACTIVE)
                        viewModel.districtCheckedItem = 1
                        dialog.cancel()
                    }
                    2 -> {
                        viewModel.getDistrictByStateCode(stateCode, CaseSort.RECOVERED)
                        viewModel.districtCheckedItem = 2
                        dialog.cancel()
                    }
                    3 -> {
                        viewModel.getDistrictByStateCode(stateCode, CaseSort.DEATHS)
                        viewModel.districtCheckedItem = 3
                        dialog.cancel()
                    }
                }
            }
            .create()
        dialog.show()
    }

    private fun initRecycleView() {
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        recycleView.addItemDecoration(VerticalSpacingItemDecoration(40))
        recycleView.adapter = adapter
    }

    private fun showProgressBar(isVisible: Boolean) {
        progress_bar.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }


}