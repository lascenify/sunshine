package com.example.android.sunshine.utilities

import com.example.android.sunshine.core.domain.*
import com.example.android.sunshine.framework.db.entities.CityEntity
import com.example.android.sunshine.framework.db.entities.ForecastEntity

object TestUtil {

    /**
     * Creates a fake ForecastListItem
     */
    fun createFakeForecastListItem()=
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

    /**
     * Creates a fake ForecastResponse of certain given coordinates
     * @param coordinates: coordinates of the city from which we want to create a fake ForecastResponse
     * @return a ForecastResponse of the given coordinates
     * */
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

    /**
     * Creates a fake ForecastEntity of certain given coordinates
     * @param coordinates: coordinates of the city from which we want to create a ForecastEntity
     * @return a ForecastEntity of the given coordinates
     */
    fun createFakeForecastEntity(coordinates: Coordinates): ForecastEntity = ForecastEntity(
            createFakeForecastResponse(coordinates)
    )

    private fun createFakeForecastListItemAtTime(timeTxt: String)=
        ForecastListItem(
            1588766400,
            null,
            null,
            ForecastMainInfo(80.15, 80.15, 80.15, 80.15, 1000.0, 1000, 1000, 63.0, -1.25),
            listOf(WeatherItem(803, "clouds", "broken clouds", "04d")),
            Clouds(97),
            Wind(9.95, 168.0),
            Sys("d"),
            timeTxt
        )



    fun createFakeForecastEntityAt12PM(): ForecastEntity{
        val listOfItems = mutableListOf<ForecastListItem>()
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-11 00:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-11 03:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-11 06:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-11 09:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-11 12:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-11 15:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-11 18:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-11 21:00:00"))
        addItemsFromNextDays(listOfItems)
        return ForecastEntity(
            1,
            listOfItems,
            null
        )
    }

    fun createFakeForecastEntityAt4PM(): ForecastEntity{
        val listOfItems = mutableListOf<ForecastListItem>()
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-11 15:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-11 18:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-11 21:00:00"))
        addItemsFromNextDays(listOfItems)
        addNItemsFromLastDay(listOfItems, 5)
        return ForecastEntity(
            1,
            listOfItems,
            null
        )
    }

    fun createFakeForecastEntityAt11PM(): ForecastEntity{
        val listOfItems = mutableListOf<ForecastListItem>()
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-11 21:00:00"))
        addItemsFromNextDays(listOfItems)
        addNItemsFromLastDay(listOfItems, 7)
        return ForecastEntity(
            1,
            listOfItems,
            null
        )
    }

    private fun addItemsFromNextDays(listOfItems: MutableList<ForecastListItem>) {
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-12 00:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-12 03:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-12 06:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-12 09:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-12 12:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-12 15:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-12 18:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-12 21:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-13 00:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-13 03:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-13 06:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-13 09:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-13 12:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-13 15:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-13 18:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-13 21:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-14 00:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-14 03:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-14 06:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-14 09:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-14 12:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-14 15:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-14 18:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-14 21:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-15 00:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-15 03:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-15 06:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-15 09:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-15 12:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-15 15:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-15 18:00:00"))
        listOfItems.add(createFakeForecastListItemAtTime("2020-05-15 21:00:00"))
    }

    private fun addNItemsFromLastDay(listOfItems: MutableList<ForecastListItem>, nItems: Int){
        val arrayOfHours = arrayListOf<String>()
        arrayOfHours.add("2020-05-15 00:00:00")
        arrayOfHours.add("2020-05-15 03:00:00")
        arrayOfHours.add("2020-05-15 06:00:00")
        arrayOfHours.add("2020-05-15 09:00:00")
        arrayOfHours.add("2020-05-15 12:00:00")
        arrayOfHours.add("2020-05-15 15:00:00")
        arrayOfHours.add("2020-05-15 18:00:00")
        arrayOfHours.add("2020-05-15 21:00:00")

        for (i in 0 until nItems)
            listOfItems.add(createFakeForecastListItemAtTime(arrayOfHours[i]))
    }
}