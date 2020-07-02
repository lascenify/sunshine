package com.example.android.sunshine.core.domain.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "hits")
    val hits: List<SearchResult>?
){
}