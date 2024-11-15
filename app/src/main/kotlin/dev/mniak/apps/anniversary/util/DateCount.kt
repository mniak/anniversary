package dev.mniak.apps.anniversary.util

import java.time.Duration
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class DateCount {
    companion object {
        fun getCount(): String {
            val today = LocalDate.now();
            val theDate = LocalDate.of(2025,1,14)

            val interval = ChronoUnit.DAYS.between(theDate, today)
            return interval.toString()
        }
    }
}