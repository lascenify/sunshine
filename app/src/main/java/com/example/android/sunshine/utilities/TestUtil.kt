package com.example.android.sunshine.utilities

import com.example.android.sunshine.core.domain.*

object TestUtil {

    private fun createFakeForecastListItem()=
        ForecastListItem(
            1588766400,
            null,
            null,
            ForecastMainInfo(80.15, 80.15, 80.15, 80.15, 1000.0, 1000, 1000, 63.0, -1.25),
            listOf(WeatherItem(803, "clouds", "broken clouds", "04d")),
            Clouds(97),
            Wind(9.95, 168.0),
            Sys("d"),
            "2020-05-11 09:00:00"
        )

    fun createFakeForecastResponse(coordinates: Coordinates): ForecastResponse{
        val forecastListItems = mutableListOf<ForecastListItem>()
        forecastListItems.add(createFakeForecastListItem())
        forecastListItems.add(createFakeForecastListItem())
        return ForecastResponse(
            "200",
            0,
            40,
            forecastListItems,
            City(6295630,
                "Globe",
                coordinates,
                0,
                1588744386,
                1588788002)
        )
    }
}