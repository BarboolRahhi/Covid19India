package com.codelectro.covid19india.ui.main.total

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.codelectro.covid19india.R
import com.codelectro.covid19india.entity.CasesSeries
import com.codelectro.covid19india.entity.StateWise
import com.codelectro.covid19india.exception.NoInternetException
import com.codelectro.covid19india.ui.MainViewModel
import com.codelectro.covid19india.ui.dateTimeFormat
import com.codelectro.covid19india.ui.formatNumber
import com.codelectro.covid19india.ui.main.MainActivity
import com.codelectro.covid19india.ui.showToast
import com.codelectro.covid19india.util.Constants.Companion.GRAPH_ANIMATION_DURATION
import com.codelectro.covid19india.util.CustomMarkerString
import com.codelectro.covid19india.util.DataState
import com.codelectro.covid19india.util.GraphDateValueStringFormatter
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
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_total.*
import timber.log.Timber


@AndroidEntryPoint
class TotalFragment : Fragment(R.layout.fragment_total) {

    companion object {
        private const val TAG = "TotalFragment"
    }

    private lateinit var viewModel: MainViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        setUpAllLineGraph()
        setUpConfirmedLineGraph()
        setUpRecoveredLineGraph()
        setUpDeathsLineGraph()

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

        // tablayout
        isTogetherGraph(true)
        isIndividualGraph(false)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        val animin =
                            AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_left)
                        val animout =
                            AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_right)
                        animin.duration = 400
                        animout.duration = 400

                        linear1.animation = animin
                        linear1.animate()

                        linear4.animation = animout
                        linear4.animate()

                        linear3.animation = animout
                        linear3.animate()

                        linear2.animation = animout
                        linear2.animate()

                        animin.start()
                        animout.start()
                        isTogetherGraph(true)
                        isIndividualGraph(false)
                    }
                    1 -> {
                        val anim =
                            AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_left)

                        val animout =
                            AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)

                        anim.duration = 400
                        linear1.animation = anim

                        linear1.animate()

                        animout.duration = 400

                        linear4.animation = animout
                        linear4.animate()

                        linear3.animation = animout
                        linear3.animate()

                        linear2.animation = animout
                        linear2.animate()
                        anim.start()
                        animout.start()
                        isTogetherGraph(false)
                        isIndividualGraph(true)
                    }
                }
            }

        })

        viewModel.getCasesSeriesData(30)
        toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (group.checkedButtonId) {
                R.id.beginning -> {
                    setGraphZoomIn()
                    hideGraphMarker()
                    viewModel.getCasesSeriesData(-1)
                }
                R.id.month -> {
                    setGraphZoomIn()
                    hideGraphMarker()
                    viewModel.getCasesSeriesData(30)
                }
                R.id.week -> {
                    setGraphZoomIn()
                    hideGraphMarker()
                    viewModel.getCasesSeriesData(14)
                }
            }
        }

    }

    private fun hideGraphMarker() {
        lineChart.highlightValue(null)
        confirmedLineChart.highlightValue(null)
        recoveredLineChart.highlightValue(null)
        deathLineChart.highlightValue(null)
    }

    private fun isTogetherGraph(isVisible: Boolean) {
        linear1.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun isIndividualGraph(isVisible: Boolean) {
        linear2.visibility = if (isVisible) View.VISIBLE else View.GONE
        linear3.visibility = if (isVisible) View.VISIBLE else View.GONE
        linear4.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun subscribeObserver() {

        viewModel.getTotalData().observe(viewLifecycleOwner, Observer {
            it?.let {
                setUI(it)
            }
        })

        viewModel.casesSeries.observe(viewLifecycleOwner, Observer {
            Timber.d("Data: CasesSerises $it")
            setRecoveredGraphData(it)
            setAllGraphData(it)
            setConfirmedGraphData(it)
            setDeathGraphData(it)
        })

    }

    private fun setGraphZoomIn() {
        lineChart.setScaleMinima(0f, 0f)
        lineChart.fitScreen()

        confirmedLineChart.setScaleMinima(0f, 0f)
        confirmedLineChart.fitScreen()

        recoveredLineChart.setScaleMinima(0f, 0f)
        recoveredLineChart.fitScreen()

        deathLineChart.setScaleMinima(0f, 0f)
        deathLineChart.fitScreen()
    }

    private fun setUpAllLineGraph() {
        lineChart.apply {
            setExtraOffsets(24f, 6f, 12f, 0f)
            setTouchEnabled(true)
            setPinchZoom(true)
            description.text = "Days"
            setNoDataText("No Data Yet!")
            animateX(GRAPH_ANIMATION_DURATION, Easing.EaseInOutQuad)
            legend.textColor = getColor(requireContext(), R.color.colorPrimary)
        }
        lineChart.xAxis.apply {
            labelRotationAngle = 0f
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            textColor = R.color.colorPrimary
        }

        lineChart.axisLeft.isEnabled = false

        lineChart.axisRight.apply {
            textColor = R.color.colorPrimary
            valueFormatter = LargeValueFormatter()
            setDrawGridLines(false)
        }
    }

    private fun setUpConfirmedLineGraph() {
        confirmedLineChart.apply {
            setExtraOffsets(24f, 6f, 12f, 0f)
            setTouchEnabled(true)
            setPinchZoom(true)
            description.text = "Days"
            setNoDataText("No Data Yet!")
            animateX(GRAPH_ANIMATION_DURATION, Easing.EaseInOutQuad)
            legend.textColor = getColor(requireContext(), R.color.colorBlueSky)
        }
        confirmedLineChart.xAxis.apply {
            labelRotationAngle = 0f
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            textColor = getColor(requireContext(), R.color.colorBlueSky)
        }

        confirmedLineChart.axisLeft.isEnabled = false

        confirmedLineChart.axisRight.apply {
            textColor = getColor(requireContext(), R.color.colorBlueSky)
            valueFormatter = LargeValueFormatter()
            setDrawGridLines(false)
        }
    }

    private fun setUpRecoveredLineGraph() {
        recoveredLineChart.apply {
            setExtraOffsets(24f, 6f, 12f, 0f)
            setTouchEnabled(true)
            setPinchZoom(true)
            description.text = "Days"
            setNoDataText("No Data Yet!")
            animateX(GRAPH_ANIMATION_DURATION, Easing.EaseInOutQuad)
            legend.textColor = getColor(requireContext(), R.color.colorGreen)
        }
        recoveredLineChart.xAxis.apply {
            labelRotationAngle = 0f
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            textColor = getColor(requireContext(), R.color.colorGreen)
        }

        recoveredLineChart.axisLeft.isEnabled = false

        recoveredLineChart.axisRight.apply {
            textColor = getColor(requireContext(), R.color.colorGreen)
            valueFormatter = LargeValueFormatter()
            setDrawGridLines(false)
        }
    }

    private fun setUpDeathsLineGraph() {
        deathLineChart.apply {
            setExtraOffsets(24f, 6f, 12f, 0f)
            setTouchEnabled(true)
            setPinchZoom(true)
            description.text = "Days"
            setNoDataText("No Data Yet!")
            animateX(GRAPH_ANIMATION_DURATION, Easing.EaseInOutQuad)
            legend.textColor = getColor(requireContext(), R.color.colorRed)
        }
        deathLineChart.xAxis.apply {
            labelRotationAngle = 0f
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            textColor = getColor(requireContext(), R.color.colorRed)
        }

        deathLineChart.axisLeft.isEnabled = false

        deathLineChart.axisRight.apply {
            textColor = getColor(requireContext(), R.color.colorRed)
            valueFormatter = LargeValueFormatter()
            setDrawGridLines(false)
        }
    }

    private fun setAllGraphData(graphDataList: List<CasesSeries>) {

        val confirmedEntries = ArrayList<Entry>()
        val recoveredEntries = ArrayList<Entry>()
        val deathEntries = ArrayList<Entry>()
        val monthsArray = ArrayList<String>()

        graphDataList.forEachIndexed { index, casesSeries ->
            monthsArray.add(casesSeries.date.substring(0, 6))
            confirmedEntries.add(Entry(index.toFloat(), casesSeries.totalconfirmed.toFloat()))
            recoveredEntries.add(Entry(index.toFloat(), casesSeries.totalrecovered.toFloat()))
            deathEntries.add(Entry(index.toFloat(), casesSeries.totaldeceased.toFloat()))
        }

        lineChart.xAxis.valueFormatter = GraphDateValueStringFormatter(monthsArray)

        val markerView = CustomMarkerString(requireContext(), R.layout.custom_marker, monthsArray)
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

        val set1: LineDataSet
        val set2: LineDataSet
        val set3: LineDataSet

        if (lineChart.data != null &&
            lineChart.data.dataSetCount > 0
        ) {
            set1 = lineChart.data.getDataSetByIndex(0) as LineDataSet
            set2 = lineChart.data.getDataSetByIndex(1) as LineDataSet
            set3 = lineChart.data.getDataSetByIndex(2) as LineDataSet
            set1.values = confirmedEntries
            set2.values = recoveredEntries
            set3.values = deathEntries
            lineChart.data.notifyDataChanged()
            lineChart.notifyDataSetChanged()
        } else {

            set1 = LineDataSet(confirmedEntries, "Confirmed")
            set1.axisDependency = AxisDependency.RIGHT
            set1.setDrawValues(false)
            set1.setDrawFilled(true)
            set1.color = getColor(requireContext(), R.color.colorAlpha60BlueSky)
            set1.setCircleColor(getColor(requireContext(), R.color.colorBlueSky))
            set1.lineWidth = 3f
            set1.fillColor = getColor(requireContext(), R.color.colorBlueSky)
            set1.fillAlpha = 30
            set1.setDrawCircleHole(false)

            set2 = LineDataSet(recoveredEntries, "Recovered")
            set2.axisDependency = AxisDependency.RIGHT
            set2.setDrawValues(false)
            set2.setDrawFilled(true)
            set2.color = getColor(requireContext(), R.color.colorAlpha60Green)
            set2.setCircleColor(getColor(requireContext(), R.color.colorAlpha60Green))
            set2.lineWidth = 3f
            set2.fillColor = getColor(requireContext(), R.color.colorAlpha60Green)
            set2.fillAlpha = 30
            set2.setDrawCircleHole(false)

            set3 = LineDataSet(deathEntries, "Deaths")
            set3.axisDependency = AxisDependency.RIGHT
            set3.setDrawValues(false)
            set3.setDrawFilled(true)
            set3.color =  getColor(requireContext(), R.color.colorAlpha60Red)
            set3.setCircleColor( getColor(requireContext(), R.color.colorRed))
            set3.lineWidth = 3f
            set3.fillColor =  getColor(requireContext(), R.color.colorRed)
            set3.fillAlpha = 30
            set3.setDrawCircleHole(false)

            // create a data object with the data sets
            val data = LineData(set1, set2, set3)
            // set data
            lineChart.data = data
        }
    }

    private fun setConfirmedGraphData(graphDataList: List<CasesSeries>) {

        val confirmedEntries = ArrayList<Entry>()
        val monthsArray = ArrayList<String>()

        graphDataList.forEachIndexed { index, casesSeries ->
            monthsArray.add(casesSeries.date.substring(0, 6))
            confirmedEntries.add(Entry(index.toFloat(), casesSeries.totalconfirmed.toFloat()))
        }


        confirmedLineChart.xAxis.valueFormatter = GraphDateValueStringFormatter(monthsArray)

        val markerView = CustomMarkerString(requireContext(), R.layout.custom_marker, monthsArray)
        confirmedLineChart.marker = markerView

        val set1: LineDataSet
        set1 = LineDataSet(confirmedEntries, "Confirmed")
        set1.axisDependency = AxisDependency.RIGHT
        set1.setDrawValues(false)
        set1.setDrawFilled(true)
        set1.color = getColor(requireContext(), R.color.colorAlpha60BlueSky)
        set1.setCircleColor(getColor(requireContext(), R.color.colorBlueSky))
        set1.lineWidth = 3f
        set1.fillColor = getColor(requireContext(), R.color.colorBlueSky)
        set1.fillAlpha = 30
        set1.setDrawCircleHole(false)


        confirmedLineChart.data = LineData(set1)
        confirmedLineChart.invalidate()

    }

    private fun setRecoveredGraphData(graphDataList: List<CasesSeries>) {

        val recoveredEntries = ArrayList<Entry>()
        val monthsArray = ArrayList<String>()

        graphDataList.forEachIndexed { index, casesSeries ->
            monthsArray.add(casesSeries.date.substring(0, 6))
            recoveredEntries.add(Entry(index.toFloat(), casesSeries.totalrecovered.toFloat()))
        }

        recoveredLineChart.xAxis.valueFormatter = GraphDateValueStringFormatter(monthsArray)

        val markerView = CustomMarkerString(requireContext(), R.layout.custom_marker, monthsArray)
        recoveredLineChart.marker = markerView

        val set2: LineDataSet
        set2 = LineDataSet(recoveredEntries, "Recovered")
        set2.axisDependency = AxisDependency.RIGHT
        set2.setDrawValues(false)
        set2.setDrawFilled(true)
        set2.color = getColor(requireContext(), R.color.colorAlpha60Green)
        set2.setCircleColor(getColor(requireContext(), R.color.colorAlpha60Green))
        set2.lineWidth = 3f
        set2.fillColor = getColor(requireContext(), R.color.colorAlpha60Green)
        set2.fillAlpha = 30
        set2.setDrawCircleHole(false)

        recoveredLineChart.data = LineData(set2)
        recoveredLineChart.invalidate()
    }

    private fun setDeathGraphData(graphDataList: List<CasesSeries>) {
        val monthsArray = ArrayList<String>()
        val deathEntries = ArrayList<Entry>()

        graphDataList.forEachIndexed { index, casesSeries ->
            monthsArray.add(casesSeries.date.substring(0, 6))
            deathEntries.add(Entry(index.toFloat(), casesSeries.totaldeceased.toFloat()))
        }

        deathLineChart.xAxis.valueFormatter = GraphDateValueStringFormatter(monthsArray)
        val markerView = CustomMarkerString(requireContext(), R.layout.custom_marker, monthsArray)
        deathLineChart.marker = markerView

        val set3: LineDataSet
        set3 = LineDataSet(deathEntries, "Deaths")
        set3.axisDependency = AxisDependency.RIGHT
        set3.setDrawValues(false)
        set3.setDrawFilled(true)
        set3.color =  getColor(requireContext(), R.color.colorAlpha60Red)
        set3.setCircleColor( getColor(requireContext(), R.color.colorRed))
        set3.lineWidth = 3f
        set3.fillColor =  getColor(requireContext(), R.color.colorRed)
        set3.fillAlpha = 30
        set3.setDrawCircleHole(false)

        deathLineChart.data = LineData(set3)

    }

    private fun setUI(total: StateWise) {
        total?.let {
            confirmedCase.text = it.confirmed.formatNumber()
            activeCase.text = it.active.formatNumber()
            recoveredCase.text = it.recovered.formatNumber()
            deathCase.text = it.deaths.formatNumber()
            deltaConfirmed.text = "+" + it.deltaconfirmed.formatNumber()
            deltaRecovered.text = "+" + it.deltarecovered.formatNumber()
            deltaDeaths.text = "+" + it.deltadeaths.formatNumber()
            lastUpdateDate.text = "Last update date: " + it.lastupdatedtime!!.dateTimeFormat()
        }

    }

    private fun showProgressBar(isVisible: Boolean) {
        progress_bar.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

}