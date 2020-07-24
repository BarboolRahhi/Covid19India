package com.codelectro.covid19india.util

import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.custom_marker.view.*
import java.text.SimpleDateFormat
import java.util.*

class CustomMarker(context: Context, layoutResource: Int):  MarkerView(context, layoutResource) {
    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        val valueY = entry?.y?.toDouble() ?: 0.0
        val valueX = entry?.x?.toDouble() ?: 0.0
        val pattern = "dd-MMM"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date = simpleDateFormat.format(Date(valueX.toLong())).toString()
        var resText = "case: $valueY"

        tvValue.text = "$date\n" + resText
        super.refreshContent(entry, highlight)
    }

    override fun getOffsetForDrawingAtPoint(xpos: Float, ypos: Float): MPPointF {
        return MPPointF(-width / 1.3f, -height - 5f)
    }
}