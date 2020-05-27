package com.example.android.sunshine.utilities

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.time.milliseconds

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

fun getLocalTimeFromTimezone(timeZoneLong: Long): String{
    val dateFormat = SimpleDateFormat("HH:mm")
    val timeZone = TimeZone.getDefault()
    timeZone.rawOffset = timeZoneLong.toInt()
    val calendar = Calendar.getInstance()
    calendar.timeZone = timeZone
    return dateFormat.format(calendar.time)
}