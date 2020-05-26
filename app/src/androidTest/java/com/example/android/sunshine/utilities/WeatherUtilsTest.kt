package com.example.android.sunshine.utilities

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import com.example.android.sunshine.R
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test


class WeatherUtilsTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @Test
    fun test_celsiusToFahrenheit() {
        val celsius = 1.0
        val fahrenheit = 33.8
        val convertedFahrenheit = WeatherUtils.celsiusToFahrenheit(celsius)
        assertThat(fahrenheit, `is`(convertedFahrenheit))
    }


    @Test
    fun test_formatTemperature() {
        val temperatureInCelsius = 24.5
        sharedPreferences.edit().putString(context.getString(R.string.pref_units_key), context.getString(R.string.pref_celsius_unit)).commit()
        val formattedTemperatureInCelsius = WeatherUtils.formatTemperature(context, temperatureInCelsius)
        assertThat(formattedTemperatureInCelsius, `is`( "25°C"))

        sharedPreferences.edit().putString(context.getString(R.string.pref_units_key), context.getString(R.string.pref_fahrenheit_unit)).commit()
        val formattedTemperatureInFahrenheit = WeatherUtils.formatTemperature(context, temperatureInCelsius)
        assertThat(formattedTemperatureInFahrenheit, `is`("76°F"))
    }

    @Test
    fun test_formatSimpleTemperature() {
        val temperatureInCelsius = 25
        sharedPreferences.edit().putString(context.getString(R.string.pref_units_key), context.getString(R.string.pref_celsius_unit)).commit()
        val formattedTemperatureInCelsius = WeatherUtils.formatSimpleTemperature(context, temperatureInCelsius)
        assertThat(formattedTemperatureInCelsius, `is`( "25°"))

        sharedPreferences.edit().putString(context.getString(R.string.pref_units_key), context.getString(R.string.pref_fahrenheit_unit)).commit()
        val formattedTemperatureInFahrenheit = WeatherUtils.formatSimpleTemperature(context, temperatureInCelsius)
        assertThat(formattedTemperatureInFahrenheit, `is`("77°"))
    }

    @Test
    fun test_getFormattedWind() {
        val windSpeed = 50.0f
        val degrees = 45.4f
        sharedPreferences.edit().putString(context.getString(R.string.pref_units_key), context.getString(R.string.pref_units_imperial)).commit()
        val formattedWindImperial = WeatherUtils.getFormattedWind(context, windSpeed, degrees)
        assertThat(formattedWindImperial, `is`("31 mph NE"))

        sharedPreferences.edit().putString(context.getString(R.string.pref_units_key), context.getString(R.string.pref_celsius_unit)).commit()
        val formattedWindMetric = WeatherUtils.getFormattedWind(context, windSpeed, degrees)
        assertThat(formattedWindMetric, `is`("50 km/h NE"))
    }

    @Test
    fun test_getIconResourceFromIconPath() {
        val iconResource = WeatherUtils.getIconResourceFromIconPath("01d")
        assertThat(iconResource, `is`("ic_01d"))

        val iconResource2 = WeatherUtils.getIconResourceFromIconPath("04n")
        assertThat(iconResource2, `is`("ic_03"))

        val iconResource3 = WeatherUtils.getIconResourceFromIconPath("11n")
        assertThat(iconResource3, `is`("ic_11"))
    }
}