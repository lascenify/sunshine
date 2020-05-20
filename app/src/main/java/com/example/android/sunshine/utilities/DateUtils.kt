package com.example.android.sunshine.utilities

import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.milliseconds

fun getDayOfWeekFromDt(dt: Long?): String{
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = dt!!
    return getDayOfWeekFromNumber(calendar.get(Calendar.DAY_OF_WEEK))
}

fun getDayOfWeekFromText(dayTxt: String): String{
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val date = simpleDateFormat.parse(dayTxt)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return getDayOfWeekFromNumber(calendar.get(Calendar.DAY_OF_WEEK))
}

private fun getDayOfWeekFromNumber(dayOfWeek: Int) =
    when(dayOfWeek){
        2 -> "Lunes"
        3 -> "Martes"
        4 -> "Miércoles"
        5 -> "Jueves"
        6 -> "Viernes"
        7 -> "Sábado"
        1 -> "Domingo"
        else -> "Lunes"
    }


fun getNextDayOfYearFromTxt(todayCompleteTxt: String): String{
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val todayDate = simpleDateFormat.parse(todayCompleteTxt)
    val calendar = Calendar.getInstance()
    calendar.time = todayDate
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    return simpleDateFormat.format(calendar.time);
}