package com.example.android.sunshine.core.domain.search

import android.text.SpannableString
import com.example.android.sunshine.core.domain.forecast.Coordinates
import com.example.android.sunshine.utilities.bold
import com.example.android.sunshine.utilities.italic
import com.example.android.sunshine.utilities.plus
import com.example.android.sunshine.utilities.spannable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResult(
    @Json(name = "locale_names")
    val localeNames: List<String>,

    @Json(name = "country")
    val country: String?,

    @Json(name = "county")
    val county: List<String>?,

    @Json(name = "administrative")
    val administrative: List<String>? = null,

    @Json(name = "country_code")
    val countryCode: String? = null,

    @Json(name = "postcode")
    val postcode: List<String?>? = null,

    @Json(name = "_geoloc")
    val coordinates: CityCoords
){
    fun getCompleteName(): SpannableString{
        return spannable {
            bold(localeNames.first() ?: "").plus(", ") +
                    bold(county?.first() ?: "").plus(", ") +
                    italic(administrative?.first() ?: "").plus(", ") +
                    italic(country ?: "")
        }
    }
}