package com.example.android.sunshine.presentation.day

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.sunshine.core.domain.OneDayForecast
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue

import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DayViewModelTest{
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var dayViewModel: DayViewModel

    @Before
    fun setup(){
        dayViewModel = DayViewModel()
    }

    @Test
    fun testDontPostTheSameValue(){
        val oneDayForecast = OneDayForecast("Monday", listOf(), 20, 10)
        assertThat(dayViewModel.forecast, notNullValue())
        dayViewModel.setDayForecast(oneDayForecast)
        //assertThat(dayViewModel.forecast.)
        dayViewModel.setDayForecast(oneDayForecast)
        assertThat(dayViewModel.forecast.value, nullValue())
    }
}