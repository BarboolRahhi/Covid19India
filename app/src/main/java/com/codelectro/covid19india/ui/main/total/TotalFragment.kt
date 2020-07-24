package com.codelectro.covid19india.ui.main.total

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.codelectro.covid19india.R
import com.codelectro.covid19india.entity.CasesSeries
import com.codelectro.covid19india.entity.StateWise
import com.codelectro.covid19india.exception.NoInternetException
import com.codelectro.covid19india.ui.MainViewModel
import com.codelectro.covid19india.ui.formatNumber
import com.codelectro.covid19india.ui.main.MainActivity
import com.codelectro.covid19india.ui.showToast
import com.codelectro.covid19india.util.CustomMarker
import com.codelectro.covid19india.util.DataState
import com.codelectro.covid19india.util.GraphDateValueFormatter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_total.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class TotalFragment : Fragment(R.layout.fragment_total) {

    companion object {
        private const val TAG = "TotalFragment"
    }

    private lateinit var viewModel: MainViewModel

    private val confirmedEntries = ArrayList<Entry>()
    private val recoveredEntries = ArrayList<Entry>()
    private val deathEntries = ArrayList<Entry>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        viewModel.covidDataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    Timber.d("Data: ${dataState.data}")
                    subscribeObserver()
                    showProgressBar(false)
                }
                is DataState.Error -> {
                    Timber.d("Data: ${dataState.exception.message}")
                    when (dataState.exception) {
                        is NoInternetException -> {
                            dataState.exception.message?.let { requireContext().showToast(it) }
                        }
                    }
                    subscribeObserver()
                    showProgressBar(false)
                }
                DataState.Loading -> {
                    Timber.d("Data: Loading")
                    showProgressBar(true)
                }
            }
        })

    }

    private fun subscribeObserver() {

        viewModel.getTotalData().observe(viewLifecycleOwner, Observer {
            it?.let {
                setUI(it)
            }
        })

        viewModel.getCasesSeriesData().observe(viewLifecycleOwner, Observer {
            Timber.d("Data: CasesSerises $it")
            setRecoveredGraphData(it)
            setAllGraphData(it)
            setConfirmedGraphData(it)
            setDeathGraphData(it)
        })

    }

    private fun setAllGraphData(graphDataList: List<CasesSeries>) {

        //lineChart.xAxis.labelRotationAngle = 0f
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.valueFormatter = GraphDateValueFormatter()

        lineChart.legend.textColor = Color.parseColor("#1530A5")

        lineChart.xAxis.setDrawGridLines(false)
        lineChart.xAxis.textColor = Color.parseColor("#1530A5")

        lineChart.axisLeft.textColor = Color.parseColor("#1530A5")
        lineChart.axisLeft.setDrawGridLines(false)


        lineChart.axisLeft.valueFormatter = LargeValueFormatter()

        lineChart.axisRight.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)

        lineChart.description.text = "Days"
        lineChart.setNoDataText("No Data yet!")

        lineChart.animateX(1500, Easing.EaseInExpo)

        val markerView = CustomMarker(requireContext(), R.layout.custom_marker)
        lineChart.marker = markerView

        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(
                e: Entry,
                h: Highlight?
            ) {
                val highlight: Array<Highlight?> =
                    arrayOfNulls<Highlight>(lineChart.data.dataSets.size)
                for (j in 0 until lineChart.data.dataSets.size) {
                    val iDataSet: IDataSet<*> = lineChart.data.dataSets[j]
                    for (i in (iDataSet as LineDataSet).values.indices) {
                        if ((iDataSet as LineDataSet).values[i].x == e.x) {
                            highlight[j] = Highlight(e.x, e.y, j)
                        }
                    }
                }
                lineChart.highlightValues(highlight)
            }

            override fun onNothingSelected() {}
        })

        val confirmedEntries = ArrayList<Entry>()
        val recoveredEntries = ArrayList<Entry>()
        val deathEntries = ArrayList<Entry>()

        graphDataList.forEach {
            val formatter = SimpleDateFormat("dd MMMM ", Locale.US)
            val date = formatter.parse(it.date)
            confirmedEntries.add(Entry(date.time.toFloat(), it.totalconfirmed.toFloat()))
            recoveredEntries.add(Entry(date.time.toFloat(), it.totalrecovered.toFloat()))
            deathEntries.add(Entry(date.time.toFloat(), it.totaldeceased.toFloat()))
        }

        val set1: LineDataSet
        val set2: LineDataSet
        val set3: LineDataSet

        if (lineChart.data != null &&
            lineChart.data.dataSetCount > 0
        ) {
            set2 = lineChart.data.getDataSetByIndex(1) as LineDataSet
            set3 = lineChart.data.getDataSetByIndex(2) as LineDataSet
            set1 = lineChart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = confirmedEntries
            set2.values = recoveredEntries
            set3.values = deathEntries
            lineChart.data.notifyDataChanged()
            lineChart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(confirmedEntries, "Confirmed")
            set1.axisDependency = AxisDependency.RIGHT
            set1.setDrawValues(false)
            set1.setDrawFilled(true)
            set1.color = Color.parseColor("#6062aeff")
            set1.setCircleColor(Color.parseColor("#62aeff"))
            set1.lineWidth = 3f
            set1.fillColor = Color.parseColor("#62aeff")
            set1.fillAlpha = 30
            set1.setDrawCircleHole(false)


            // create a dataset and give it a type
            set2 = LineDataSet(recoveredEntries, "Recovered")
            set2.axisDependency = AxisDependency.RIGHT
            set2.setDrawValues(false)
            set2.setDrawFilled(true)
            set2.color = Color.parseColor("#6028a745")
            set2.setCircleColor(Color.parseColor("#28a745"))
            set2.lineWidth = 3f
            set2.fillColor = Color.parseColor("#28a745")
            set2.fillAlpha = 30
            set2.setDrawCircleHole(false)


            set3 = LineDataSet(deathEntries, "Deaths")
            set3.axisDependency = AxisDependency.RIGHT
            set3.setDrawValues(false)
            set3.setDrawFilled(true)
            set3.color = Color.parseColor("#60e23129")
            set3.setCircleColor(Color.parseColor("#e23129"))
            set3.lineWidth = 3f
            set3.fillColor = Color.parseColor("#e23129")
            set3.fillAlpha = 30
            set3.setDrawCircleHole(false)

            // create a data object with the data sets
            val data = LineData(set1, set2, set3)
            // data.setValueTextColor(Color.WHITE)
            // data.setValueTextSize(9f)
            data.setDrawValues(false)


            // set data
            lineChart.data = data
        }
    }

    private fun setConfirmedGraphData(graphDataList: List<CasesSeries>) {

        graphDataList.forEach {
            val formatter = SimpleDateFormat("dd MMMM ", Locale.US)
            val date = formatter.parse(it.date)
            confirmedEntries.add(Entry(date.time.toFloat(), it.totalconfirmed.toFloat()))
        }

        val vl = LineDataSet(confirmedEntries, "Confirmed Cases")
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.color = Color.parseColor("#6062aeff")
        vl.setCircleColor(Color.parseColor("#62aeff"))
        vl.lineWidth = 3f
        vl.fillColor = Color.parseColor("#62aeff")
        vl.fillAlpha = 30
        vl.setDrawCircleHole(false)


        confirmedLineChart.xAxis.labelRotationAngle = 0f
        confirmedLineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        confirmedLineChart.xAxis.valueFormatter = GraphDateValueFormatter()

        confirmedLineChart.legend.textColor = Color.parseColor("#62aeff")

        confirmedLineChart.xAxis.setDrawGridLines(false)
        confirmedLineChart.xAxis.textColor = Color.parseColor("#62aeff")

        confirmedLineChart.axisLeft.textColor = Color.parseColor("#62aeff")
        confirmedLineChart.axisLeft.setDrawGridLines(false)
        confirmedLineChart.axisLeft.valueFormatter = LargeValueFormatter()

        confirmedLineChart.data = LineData(vl)

        confirmedLineChart.axisRight.isEnabled = false

        confirmedLineChart.setTouchEnabled(true)
        confirmedLineChart.setPinchZoom(true)

        confirmedLineChart.description.text = "Days"
        confirmedLineChart.setNoDataText("No Data yet!")

        confirmedLineChart.animateX(1500, Easing.EaseInExpo)

        val markerView = CustomMarker(requireContext(), R.layout.custom_marker)
        confirmedLineChart.marker = markerView

    }

    private fun setRecoveredGraphData(graphDataList: List<CasesSeries>) {

        graphDataList?.forEach {
            val formatter = SimpleDateFormat("dd MMMM ")
            val date = formatter.parse(it.date)
            recoveredEntries.add(Entry(date.time.toFloat(), it.totalrecovered!!.toFloat()))
        }

        val vl = LineDataSet(recoveredEntries, "Recovered Cases")

        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.color = Color.parseColor("#6028a745")
        vl.setCircleColor(Color.parseColor("#28a745"))
        vl.lineWidth = 3f
        vl.fillColor = Color.parseColor("#28a745")
        vl.fillAlpha = 30
        vl.setDrawCircleHole(false)

        recoveredLineChart.xAxis.labelRotationAngle = 0f
        recoveredLineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        recoveredLineChart.xAxis.valueFormatter = GraphDateValueFormatter()

        recoveredLineChart.legend.textColor = Color.parseColor("#28a745")

        recoveredLineChart.xAxis.setDrawGridLines(false)
        recoveredLineChart.xAxis.textColor = Color.parseColor("#28a745")

        recoveredLineChart.axisLeft.textColor = Color.parseColor("#28a745")
        recoveredLineChart.axisLeft.setDrawGridLines(false)

        recoveredLineChart.axisLeft.valueFormatter = LargeValueFormatter()

        recoveredLineChart.data = LineData(vl)

        recoveredLineChart.axisRight.isEnabled = false

        recoveredLineChart.setTouchEnabled(true)
        recoveredLineChart.setPinchZoom(true)

        recoveredLineChart.description.text = "Days"
        recoveredLineChart.setNoDataText("No Data yet!")

        recoveredLineChart.animateX(1500, Easing.EaseInExpo)

        val markerView = CustomMarker(requireContext(), R.layout.custom_marker)
        recoveredLineChart.marker = markerView

    }

    private fun setDeathGraphData(graphDataList: List<CasesSeries>) {

        graphDataList?.forEach {
            val formatter = SimpleDateFormat("dd MMMM ")
            val date = formatter.parse(it.date)
            deathEntries.add(Entry(date.time.toFloat(), it.totaldeceased!!.toFloat()))
        }

        val vl = LineDataSet(deathEntries, "Death Cases")

        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.color = Color.parseColor("#60e23129")
        vl.setCircleColor(Color.parseColor("#e23129"))
        vl.lineWidth = 3f
        vl.fillColor = Color.parseColor("#e23129")
        vl.fillAlpha = 30
        vl.setDrawCircleHole(false)

        deathLineChart.xAxis.labelRotationAngle = 0f
        deathLineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        deathLineChart.xAxis.valueFormatter = GraphDateValueFormatter()

        deathLineChart.legend.textColor = Color.parseColor("#e23129")

        deathLineChart.xAxis.setDrawGridLines(false)
        deathLineChart.xAxis.textColor = Color.parseColor("#e23129")

        deathLineChart.axisLeft.textColor = Color.parseColor("#e23129")
        deathLineChart.axisLeft.setDrawGridLines(false)

        deathLineChart.axisLeft.valueFormatter = LargeValueFormatter()

        deathLineChart.data = LineData(vl)

        deathLineChart.axisRight.isEnabled = false

        deathLineChart.setTouchEnabled(true)
        deathLineChart.setPinchZoom(true)

        deathLineChart.description.text = "Days"
        deathLineChart.setNoDataText("No Data yet!")

        deathLineChart.animateX(1500, Easing.EaseInExpo)

        val markerView = CustomMarker(requireContext(), R.layout.custom_marker)
        deathLineChart.marker = markerView

    }


    private fun setUI(total: StateWise) {
        total?.let {
            confirmedCase.text = it.confirmed.formatNumber()
            activeCase.text = it.active.formatNumber()
            recoveredCase.text = it.recovered.formatNumber()
            deathCase.text = it.deaths.formatNumber()
        }

    }

    private fun showProgressBar(isVisible: Boolean) {
        progress_bar.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

}