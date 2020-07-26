package com.codelectro.covid19india.util

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class GraphDateValueStringFormatter(
    private val array: ArrayList<String>
) : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return try {
            array[value.toInt()]
        } catch (e: Exception) {
            ""
        }

    }

}