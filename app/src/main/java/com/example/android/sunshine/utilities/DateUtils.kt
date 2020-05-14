package com.example.android.sunshine.utilities

import java.text.SimpleDateFormat
import java.util.*

fun getDayOfWeekFromDt(dt: Long?): String{
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = dt!!
    return getDayOfWeekFromNumber(calendar.get(Calendar.DAY_OF_WEEK))
}

private fun getDayOfWeekFromNumber(dayOfWeek: Int) =
    when(dayOfWeek){
        1 -> "Lunes"
        2 -> "Martes"
        3 -> "Miércoles"
        4 -> "Jueves"
        5 -> "Viernes"
        6 -> "Sábado"
        7 -> "Domingo"
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