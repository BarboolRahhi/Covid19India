package com.codelectro.covid19india.ui

import android.content.Context
import android.widget.Toast
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Int.formatNumber(): String {
    return NumberFormat.getIntegerInstance().format(this).toString()
}

fun String.dateTimeFormat(): String {
    val date = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH).parse(this)
    date?.let {
        return SimpleDateFormat("dd MMM, yyyy h:mm a", Locale.ENGLISH).format(date)
    }
    return "No Date Time"
}
