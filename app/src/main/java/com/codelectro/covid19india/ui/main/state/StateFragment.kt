package com.codelectro.covid19india.ui.main.state

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.codelectro.covid19india.R
import com.codelectro.covid19india.adapters.StateRecyclerAdapter
import com.codelectro.covid19india.entity.StateWise
import com.codelectro.covid19india.exception.NoInternetException
import com.codelectro.covid19india.ui.MainViewModel
import com.codelectro.covid19india.ui.main.MainActivity
import com.codelectro.covid19india.ui.showToast
import com.codelectro.covid19india.util.CaseSort
import com.codelectro.covid19india.util.DataState
import com.codelectro.covid19india.util.VerticalSpacingItemDecoration
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_state.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class StateFragment : Fragment(R.layout.fragment_state) {

    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var adapter: StateRecyclerAdapter

    private var menu: Menu? = null

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

        val navController = Navigation.findNavController(view)
        viewModel = (activity as MainActivity).viewModel
        viewModel.districtCheckedItem = 0
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
        when(viewModel.stateCheckedItem) {
            0 -> viewModel.getStateWiseData(CaseSort.CONFIRMED)
            1 -> viewModel.getStateWiseData(CaseSort.ACTIVE)
            2 -> viewModel.getStateWiseData(CaseSort.RECOVERED)
            3 -> viewModel.getStateWiseData(CaseSort.DEATHS)
        }
        viewModel.stateWise.observe(viewLifecycleOwner, Observer {
            adapter.setStateList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
        this.menu = menu
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
            .setSingleChoiceItems(items, viewModel.stateCheckedItem) { dialog, which ->
                when(which) {
                    0 -> {
                        viewModel.getStateWiseData(CaseSort.CONFIRMED)
                        viewModel.stateCheckedItem = 0
                        dialog.cancel()
                    }
                    1 -> {
                        viewModel.getStateWiseData(CaseSort.ACTIVE)
                        viewModel.stateCheckedItem = 1
                        dialog.cancel()
                    }
                    2 -> {
                        viewModel.getStateWiseData(CaseSort.RECOVERED)
                        viewModel.stateCheckedItem = 2
                        dialog.cancel()
                    }
                    3 -> {
                        viewModel.getStateWiseData(CaseSort.DEATHS)
                        viewModel.stateCheckedItem = 3
                        dialog.cancel()
                    }
                }
            }
            .create()
        dialog.show()
    }

    private fun initRecycleView() {
        recycleView.isNestedScrollingEnabled = false
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        recycleView.addItemDecoration(VerticalSpacingItemDecoration(40))
        recycleView.adapter = adapter
    }


    private fun showProgressBar(isVisible: Boolean) {
        progress_bar.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }


}