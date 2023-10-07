package com.example.skyscan.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface API_Interface {
    @GET("forecast.json")
    fun getweatherdata(
        @Query("KEY") key:String,
        @Query("q") location:String,
        @Query("aqi") aqi :String="no"

    ): Call<weatherdata>

}