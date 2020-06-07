package com.codelectro.covid19india.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class VerticalSpacingItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = verticalSpaceHeight

        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = verticalSpaceHeight
        } else {
            outRect.top = 0
        }
    }

}