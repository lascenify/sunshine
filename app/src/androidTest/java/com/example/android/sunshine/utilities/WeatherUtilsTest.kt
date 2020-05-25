package com.example.android.sunshine.utilities

import org.junit.Test

import org.junit.Assert.*

class WeatherUtilsTest {

    @Test
    fun celsiusToFahrenheit() {
        val celsius = 1.0
        val fahrenheit = 33.8
        val convertedFahrenheit = WeatherUtils.celsiusToFahrenheit(celsius)
        assert(fahrenheit == convertedFahrenheit)
    }

    @Test
    fun fahrenheitToCelsius() {
    }

    @Test
    fun formatTemperature() {
    }

    @Test
    fun formatSimpleTemperature() {
    }

    @Test
    fun getFormattedWind() {
    }

    @Test
    fun getStringForWeatherCondition() {
    }

    @Test
    fun getIconResourceFromIconPath() {
    }
}