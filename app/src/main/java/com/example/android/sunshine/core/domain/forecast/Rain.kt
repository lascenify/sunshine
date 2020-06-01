package com.example.android.sunshine.core.domain.forecast

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Rain(
    @Json(name = "3h")
    // Rain volume for last 3 hours, mm
    val jsonMember3h: Double?
): Parcelable