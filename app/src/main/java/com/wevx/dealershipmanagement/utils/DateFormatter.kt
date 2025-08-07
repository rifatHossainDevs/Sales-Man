package com.wevx.dealershipmanagement.utils

import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DateFormatter {
    fun formatDateToDDMMYY(isoDate: String): String {
        // Parse the ISO 8601 date string
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC") // because of 'Z' (Zulu time)

        val date = parser.parse(isoDate)

        // Format to DD-MM-YY
        val formatter = SimpleDateFormat("dd-MM-yy", Locale.getDefault())

        return formatter.format(date)
    }
}