package com.example.android.sunshine.framework.db.entities

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.android.sunshine.core.domain.ForecastListItem
import com.example.android.sunshine.core.domain.ForecastResponse
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "Forecast")
data class ForecastEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "list")
    // list of forecast. In case of 5 day weather forecast, 40 elements in this list (each 3 hours)
    val list: List<ForecastListItem>?,

    @Embedded
    val city: CityEntity?) : Parcelable{

    @Ignore
    constructor(forecastResponse: ForecastResponse): this(
        id = 0,
        list = forecastResponse.list,
        city = forecastResponse.city?.let { CityEntity(it) }
    )
}