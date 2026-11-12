package com.example.townpass.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtils {
    private val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
    private val fullDateTimeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())

    fun formatDate(millis: Long): String {
        if (millis <= 0) return ""
        return dateFormat.format(Date(millis))
    }

    fun formatDateTime(millis: Long): String {
        if (millis <= 0) return ""
        return dateTimeFormat.format(Date(millis))
    }

    fun formatFullDateTime(millis: Long): String {
        if (millis <= 0) return ""
        return fullDateTimeFormat.format(Date(millis))
    }

    fun formatRange(startMillis: Long, endMillis: Long): String {
        if (startMillis <= 0 && endMillis <= 0) return ""
        val start = if (startMillis > 0) formatDate(startMillis) else ""
        val end = if (endMillis > 0) formatDate(endMillis) else ""
        return if (start.isNotEmpty() && end.isNotEmpty()) {
            "$start - $end"
        } else {
            start.ifEmpty { end }
        }
    }
}
