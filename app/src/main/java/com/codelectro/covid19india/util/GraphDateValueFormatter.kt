package com.codelectro.covid19india.util

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class GraphDateValueFormatter : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val pattern = "dd-MMM"
        val simpleDateFormat = SimpleDateFormat(pattern)
        Timber.d("Date Custom: ${simpleDateFormat.format(Date(value.toLong()))}")
        return simpleDateFormat.format(Date(value.toLong())).toString()
    }

}