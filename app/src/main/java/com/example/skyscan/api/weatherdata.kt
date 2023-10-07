package com.example.skyscan.api

data class weatherdata(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)