package com.codelectro.covid19india.util

import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.custom_marker.view.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CustomMarkerString(
    context: Context,
    layoutResource: Int,
    val array: ArrayList<String>
):  MarkerView(context, layoutResource) {
    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        val valueY = entry?.y?.toDouble() ?: 0.0
        val valueX = entry?.x ?: 0.0.toFloat()
        val resText = "${array[valueX.toInt()]}\ncase: $valueY"
        tvValue.text = resText
        super.refreshContent(entry, highlight)
    }

    override fun getOffsetForDrawingAtPoint(xpos: Float, ypos: Float): MPPointF {
        return MPPointF(-width / 2f, -height - 10f)
    }
}