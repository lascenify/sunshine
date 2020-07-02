package com.example.android.sunshine.utilities

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Given a date in format txt, returns the day of the week.
 * @param dayTxt: complete date in txt. For example: 2020-05-22
 * @return string with the day of the week. For example, Friday
 */
fun getDayOfWeekFromText(dayTxt: String): String{
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val date = simpleDateFormat.parse(dayTxt)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return getDayOfWeekFromNumber(calendar.get(Calendar.DAY_OF_WEEK))
}

/**
 * Returns the day name of the week from a given number (1-7)
 * @param dayOfWeek: day of week in number, starting in Sunday
 * @return name of the day of the week
 */
private fun getDayOfWeekFromNumber(dayOfWeek: Int) =
    when(dayOfWeek){
        2 -> "Monday"
        3 -> "Tuesday"
        4 -> "Wednesday"
        5 -> "Thursday"
        6 -> "Friday"
        7 -> "Saturday"
        1 -> "Sunday"
        else -> "Monday"
    }

/**
 * Returns the next day of the calendar from a date in txt.
 * @param todayCompleteTxt: the date of the calendar from which we want to know the next day
 * @return the next day of the calendar, in string format
 */
fun getNextDayOfYearFromTxt(todayCompleteTxt: String): String{
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val todayDate = simpleDateFormat.parse(todayCompleteTxt)
    val calendar = Calendar.getInstance()
    calendar.time = todayDate
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    return simpleDateFormat.format(calendar.time);
}

/**
 * Returns the local time given a timeZone
 * @param shiftFromUtcInSecs: shift in seconds from UTC
 */
@RequiresApi(Build.VERSION_CODES.O)
fun getLocalTimeFromTimezone(shiftFromUtcInSecs: Long): String{
    val localDate = LocalDateTime.now(ZoneId.of("UTC"))
    val zoneOffset = ZoneOffset.ofTotalSeconds(shiftFromUtcInSecs.toInt())
    val dateAtOffset = localDate.atOffset(zoneOffset)
    return formatDateAtOffset(dateAtOffset)
}

/**
 * Given a date with offset, returns a string with the local time in format "HH:mm"
 * For example, given OffsetDateTime with time 09:00+01:00, returns "10:00"
 * @param date: date with offset
 */
@RequiresApi(Build.VERSION_CODES.O)
fun formatDateAtOffset(date: OffsetDateTime): String{
    val offset = date.offset
    val hours = offset.toString().dropLast(3).drop(0).toLong()
    val sign = offset.toString()[0]
    var time :OffsetDateTime? = null
    if (sign == '-'){
        time = date.minusHours(hours)
    } else if (sign == '+'){
        time = date.plusHours(hours)
    }
    if (time != null) {
        return time.format(DateTimeFormatter.ISO_LOCAL_TIME).substring(0, 5)
    }
    else return ""
}