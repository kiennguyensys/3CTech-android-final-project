package com.yoshitoke.weatheringwithyou.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

fun Int.unixTimestampToString(format: String) : String {

    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this*1000.toLong()

        val outputDateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time)

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toString()
}


fun Double.kelvinToCelsius() : Int {
    return (this - 273.15).toInt()
}

fun Int.toCelsiusString() : String {
    return "$this°C"
}

fun Int.toPercentageString() : String {
    return "$this %"
}

fun Double.toVelocityString() : String {
    return "$this m/s"
}

fun String.toFirstCapString() : String {
    return this.substring(0, 1).toUpperCase(Locale.ROOT) + this.substring(1)
}
