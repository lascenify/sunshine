package com.example.android.sunshine.core.domain.forecast

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Snow(
    @Json(name = "3h")
    val jsonMember3h: Double?
): Parcelable