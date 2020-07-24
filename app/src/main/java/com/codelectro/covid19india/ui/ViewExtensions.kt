package com.codelectro.covid19india.ui

import android.content.Context
import android.widget.Toast
import java.text.NumberFormat

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Int.formatNumber() : String {
    return NumberFormat.getIntegerInstance().format(this).toString()
}

