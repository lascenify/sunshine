package com.example.android.sunshine.utilities

import android.text.format.DateUtils
import org.junit.Test

class DateUtilsTest {

    @Test
    fun testGetDayOfWeekFromText(){
        val dayOfWeek = getDayOfWeekFromText("2020-05-22")
        assert(dayOfWeek == "Friday")

        val secondDayOfWeek = getDayOfWeekFromText("2020-05-23")
        assert(secondDayOfWeek == "Saturday")
    }


    @Test
    fun testGetNextDayOfYearFromTxt(){
        val nextDay = getNextDayOfYearFromTxt("2020-05-22")
        assert(nextDay == "2020-05-23")

        val nextDay2 = getNextDayOfYearFromTxt("2020-05-31")
        assert(nextDay2 == "2020-06-01")

        val nextDay3 = getNextDayOfYearFromTxt("2020-12-31")
        assert(nextDay3 == "2021-01-01")
    }
}