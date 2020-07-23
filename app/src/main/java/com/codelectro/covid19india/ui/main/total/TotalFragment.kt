package com.codelectro.covid19india.ui.main.total

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.core.cartesian.series.Line
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import com.codelectro.covid19india.R
import com.codelectro.covid19india.models.GraphData
import com.codelectro.covid19india.models.Total
import com.codelectro.covid19india.ui.main.MainActivity
import com.codelectro.covid19india.ui.showToast
import com.codelectro.covid19india.util.ApiResult
import com.codelectro.covid19india.util.CustomMarker
import com.codelectro.covid19india.util.GraphDateValueFormatter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.fragment_total.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class TotalFragment : Fragment() {

    companion object {
        private const val TAG = "TotalFragment"
    }


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewmodel: TotalViewModel

    private val entries = ArrayList<Entry>()

    private val stringDateList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_total, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainComponent.inject(this)
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel = ViewModelProvider(this, viewModelFactory).get(TotalViewModel::class.java)

        subscribeObserver()

        // mp chart



//Part2
//        entries.add(Entry(10f, 10f))
//        entries.add(Entry(20f, 2f))
//        entries.add(Entry(30f, 7f))
//        entries.add(Entry(40f, 15f))
//        entries.add(Entry(50f, 20f))



        // webview
//        val wedData: String =  "<div class=\"flourish-embed flourish-chart\" data-src=\"story/230114\"><script src=\"https://public.flourish.studio/resources/embed.js\"></script></div>"
//        val mimeType: String = "text/html"
//        val utfType: String = "UTF-8"
//        webView.webViewClient = WebViewClient()
//        webView.settings.javaScriptEnabled = true
//        webView.settings.javaScriptCanOpenWindowsAutomatically = true
//        webView.loadData(wedData,mimeType,utfType)

    }

    private fun subscribeObserver() {
        viewmodel.getSummary().removeObservers(viewLifecycleOwner)
        viewmodel.getSummary().observe(viewLifecycleOwner, Observer {
            execute(it)
        })

        viewmodel.getGraphDataList().removeObservers(viewLifecycleOwner)
        viewmodel.getGraphDataList().observe(viewLifecycleOwner, Observer {
            executeGraph(it)
        })

    }

    private fun execute(apiResult: ApiResult<Total?>) {
        when (apiResult) {
            is ApiResult.Success -> setUI(apiResult.value)
            is ApiResult.GenericError -> Log.d(
                MainActivity.TAG,
                "onCreate: ${apiResult.errorMessage}"
            )
            ApiResult.NetworkError -> {
                requireContext().showToast("Network Error! Please check your internet connection.")
                Log.d(MainActivity.TAG, "onCreate: NetworkError")
            }
        }
    }

    private fun executeGraph(apiResult: ApiResult<List<GraphData>?>) {
        when (apiResult) {
            is ApiResult.Success -> setGraphData(apiResult.value)
            is ApiResult.GenericError -> Log.d(
                MainActivity.TAG,
                "onCreate: ${apiResult.errorMessage}"
            )
            ApiResult.NetworkError -> Log.d(MainActivity.TAG, "onCreate: NetworkError")
        }
    }

    private fun setGraphData(graphDataList: List<GraphData>?) {

        Log.d(TAG, "setGraphData: $graphDataList")


        anyChartView.setProgressBar(progressBar3)
        val cartesian = AnyChart.line()

        cartesian.animation(true)
        cartesian.crosshair().enabled(true)
        cartesian.crosshair()
            .yLabel(true) // TODO ystroke
            .yStroke(null as Stroke?, null, null, null as String?, null as String?)

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)

        cartesian.title("The chart show daily and total case trends in India")

        cartesian.yAxis(0).title("Number of Cases")
        // cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)


        val seriesData: MutableList<DataEntry> = ArrayList()

        graphDataList?.forEach {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val date = formatter.parse(it.x)
            val pattern = "MM-dd"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val sdf = simpleDateFormat.format(Date(date.time)).toString()
            stringDateList.add(sdf)
            entries.add(Entry(date.time.toFloat(), it.value!!.toFloat()))
            val graphData = GraphData(dataFormatter(it.x!!), it.value, it.value2, it.value3)
            seriesData.add(graphData)
        }

        val set = Set.instantiate()
        set.data(seriesData)
        val series1Mapping = set.mapAs("{ x: 'x', value: 'value' }")
        val series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }")
        val series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }")

        val series1: Line = cartesian.line(series1Mapping)
        series1.name("Confirmed")
        series1.hovered().markers().enabled(true)
        series1.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series1.color("#00d8d6")
        series1.tooltip()
            .position("left")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(3.0)
            .offsetY(3.0)

        val series2: Line = cartesian.line(series2Mapping)
        series2.name("Death")
        series2.hovered().markers().enabled(true)
        series2.color("#ff4d4d")
        series2.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series2.tooltip()
            .position("left")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(3.0)
            .offsetY(3.0)

        val series3: Line = cartesian.line(series3Mapping)
        series3.name("Recovered")
        series3.hovered().markers().enabled(true)
        series3.color("#05c46b")
        series3.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series3.tooltip()
            .position("left")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(3.0)
            .offsetY(3.0)

        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)

        anyChartView.setChart(cartesian)


        //Part3
        val vl = LineDataSet(entries, "My Type")


//Part4
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = R.color.colorAccent
        vl.fillAlpha = R.color.colorPrimary

//Part5
        lineChart.xAxis.labelRotationAngle = 0f
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.valueFormatter = GraphDateValueFormatter()
        lineChart.axisLeft.valueFormatter = LargeValueFormatter()

//Part6
        lineChart.data = LineData(vl)

//Part7
        lineChart.axisRight.isEnabled = false

//Part8
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)

//Part9
        lineChart.description.text = "Days"
        lineChart.setNoDataText("No forex yet!")

//Part10
        lineChart.animateX(1800, Easing.EaseInExpo)

        val markerView = CustomMarker(requireContext(), R.layout.custom_marker)
        lineChart.marker = markerView

        //end


    }

    private fun dataFormatter(date: String): String {
        val pattern = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(pattern)
        return simpleDateFormat.format(
            SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.ENGLISH
            ).parse(date)
        )
    }

    private fun setUI(total: Total?) {
        total?.let {
            confirmedCase.text = it.confirmed.toString()
            activeCase.text = it.active.toString()
            recoveredCase.text = it.recovered.toString()
            deathCase.text = it.deaths.toString()
        }

    }

    private class CustomDataEntry internal constructor(
        x: String?,
        value: Number?,
        value2: Number?,
        value3: Number?
    ) :
        ValueDataEntry(x, value) {
        init {
            setValue("value2", value2)
            setValue("value3", value3)
        }
    }


}