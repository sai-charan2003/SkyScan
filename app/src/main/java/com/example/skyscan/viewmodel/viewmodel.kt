package com.example.skyscan.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.skyscan.R
import com.example.skyscan.api.API_Interface
import com.example.skyscan.api.weatherdata
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class viewmodel(location:String):ViewModel() {
    var cityname by
    mutableStateOf("")
    var userlocation by mutableStateOf(location)

    var currenttemperature by
    mutableStateOf("")
    var currenttemperature_F by
    mutableStateOf("")
    var currentfeelslike by
    mutableStateOf("")
    var des by
    mutableStateOf("")
    var sunrise by
    mutableStateOf("")
    var sunset by
    mutableStateOf("")
    var isloading by mutableStateOf<Boolean>(true)
    var isday by
    mutableStateOf(0)
    var imagecode by
    mutableStateOf(0)
    var feelslike by
    mutableStateOf("")
    var feelslike_F by
    mutableStateOf("")

    var humidity by
    mutableStateOf("")
    var maxtemp by
    mutableStateOf("")
    var maxtemp_F by
    mutableStateOf("")
    var mintemp by
    mutableStateOf("")
    var mintemp_F by
    mutableStateOf("")

    data class HourlyForecast(
        val time: String,

        val tempCelsius: Double,
        val tempFahrenheit: Double,
        val conditionText: String,
        val conditionIcon: String,
        val isday: Int,


        )

    val hourlyForecasts: MutableList<HourlyForecast> = mutableListOf()
    fun getweatherdata() {

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.weatherapi.com/v1/")
            .build()
            .create(API_Interface::class.java)
        var response = retrofit.getweatherdata(
            key = "ff200d03083c4e7891263736231801", location = userlocation
        ).enqueue(object : Callback<weatherdata> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(call: Call<weatherdata>, response: Response<weatherdata>) {
                Log.d("TAG", "onResponse: $userlocation")
                val responseBody = response.body()
                if (responseBody != null) {
                    hourlyForecasts.clear()
                    cityname = responseBody.location.name
                    Log.d("TAG", "onResponse: $responseBody")
                    currenttemperature = responseBody.current.temp_c.toInt().toString()
                    currenttemperature_F = responseBody.current.temp_f.toInt().toString()

                    des = responseBody.current.condition.text
                    isloading = false
                    isday = responseBody.current.is_day
                    sunset = responseBody.forecast.forecastday[0].astro.sunset
                    sunrise = responseBody.forecast.forecastday[0].astro.sunrise
                    maxtemp = responseBody.forecast.forecastday[0].day.maxtemp_c.toInt().toString()
                    maxtemp_F = responseBody.forecast.forecastday[0].day.maxtemp_f.toInt().toString()
                    feelslike = responseBody.current.feelslike_c.toInt().toString()
                    feelslike_F = responseBody.current.feelslike_f.toInt().toString()
                    humidity=responseBody.current.humidity.toString()
                    mintemp=responseBody.forecast.forecastday[0].day.mintemp_c.toInt().toString()
                    mintemp_F=responseBody.forecast.forecastday[0].day.mintemp_f.toInt().toString()



                    if (isday == 1 && des == "Sunny") {
                        imagecode = R.drawable.sunny

                    } else if (isday == 0 && des == "Clear") {
                        imagecode = R.drawable.moon
                    } else if ((isday == 1 && des == "Partly cloudy") || (isday == 1 && des == "Cloudy") || (isday == 1 && des == "Overcast")) {
                        imagecode = R.drawable.dpcloudy

                    } else if ((isday == 0 && des == "Partly cloudy") || (isday == 0 && des == "Cloudy") || (isday == 0 && des == "Overcast")) {
                        imagecode = R.drawable.ncloudy
                    } else if (isday == 1 && des == "Mist" || (isday == 1 && des == "Fog" || (isday == 1 && des == "Freezing fog"))) {
                        imagecode = R.drawable.dfoggy
                    } else if (isday == 0 && des == "Mist" || (isday == 0 && des == "Fog") || (isday == 0 && des == "Freezing fog")) {
                        imagecode = R.drawable.ncloudy
                    } else if ((isday == 1 && des == "Patchy rain possible") || (isday == 1 && des == "Patchy freezing drizzle possible") || (isday == 1 && des == "Patchy light drizzle") || (isday == 1 && des == "Light drizzle") || (isday == 1 && des == "Freezing drizzle") || (isday == 1 && des == "Patchy rain") || (isday == 0 && des == "Light Rain")) {
                        imagecode = R.drawable.dlrain
                    } else if ((isday == 0 && des == "Patchy rain possible") || (isday == 0 && des == "Patchy freezing drizzle possible") || (isday == 0 && des == "Patchy light drizzle") || (isday == 1 && des == "Light drizzle") || (isday == 0 && des == "Freezing drizzle") || (isday == 0 && des == "Patchy rain") || (isday == 0 && des == "Light rain")) {
                        imagecode = R.drawable.nlrain_0
                    } else if ((isday == 1 && des == "Patchy snow possible") || isday == 1 && des == "Patchy sleet possible" || (isday == 1 && des == "Blowing snow") || (isday == 1 && des == "Blizzard")) {
                        imagecode = R.drawable.dsnow
                    } else if ((isday == 0 && des == "Patchy snow possible") || isday == 0 && des == "Patchy sleet possible" || (isday == 0 && des == "Blowing snow" || (isday == 0 && des == "Blizzard"))) {
                        imagecode = R.drawable.nsnow
                    } else if (isday == 1 && des == "Thundery outbreaks possible") {
                        imagecode = R.drawable.dthunder
                    } else if (isday == 0 && des == "Thundery outbreaks possible") {
                        imagecode = R.drawable.nthunder
                    } else if (isday == 1 && des == "Heavy freezing drizzle" || (isday == 1 && des == "Moderate rain at times") || (isday == 1 && des == "Moderate rain") || (isday == 1 && des == "Heavy rain at times") || (isday == 1 && des == "Heavy rain") || (isday == 1 && des == "Moderate or heavy freezing rain")) {
                        imagecode = R.drawable.drain
                    } else if (isday == 0 && des == "Heavy freezing drizzle" || (isday == 0 && des == "Moderate rain at times") || (isday == 1 && des == "Moderate rain") || (isday == 0 && des == "Heavy rain at times") || (isday == 0 && des == "Heavy rain") || (isday == 0 && des == "Moderate or heavy freezing rain")) {
                        imagecode = R.drawable.nrain
                    }
                    for (i in 0..23) {
                        val now = responseBody.forecast.forecastday[0].hour[i]
                        val current = responseBody.forecast.forecastday[0].hour[i].time_epoch
                        val currentEpochTime = System.currentTimeMillis() / 1000




                        if (current > currentEpochTime) {

                            hourlyForecasts.add(
                                HourlyForecast(
                                    now.time,
                                    now.temp_c,
                                    now.temp_f,
                                    now.condition.text,
                                    now.condition.icon,
                                    now.is_day
                                )
                            )
                        }


                    }


                }


            }

            override fun onFailure(call: Call<weatherdata>, t: Throwable) {

            }

        })


    }
}
fun getweathericon(isday:Int,des:String) :Int {
    var icon:Int=0
    if(isday==1&&des=="Sunny"){
        icon= R.drawable.sunny

    }


    else if(isday==0&&des=="Clear"){
        icon=R.drawable.moon
    }
    else if((isday==1&&des=="Partly cloudy")||(isday==1&&des=="Cloudy")||(isday==1&&des=="Overcast")){
        icon= R.drawable.dpcloudy

    }
    else if((isday==0&&des=="Partly cloudy")||(isday==0&&des=="Cloudy")||(isday==0&&des=="Overcast")){
        icon= R.drawable.ncloudy
    }
    else if(isday==1&&des=="Mist"||(isday==1&&des=="Fog"||(isday==1&&des=="Freezing fog"))){
        icon= R.drawable.dfoggy
    }
    else if(isday==0&&des=="Mist"||(isday==0&&des=="Fog")||(isday==0&&des=="Freezing fog")){
        icon= R.drawable.ncloudy
    }
    else if((isday==1&&des=="Patchy rain possible")||(isday==1&&des=="Patchy freezing drizzle possible")||(isday==1&&des=="Patchy light drizzle")||(isday==1&&des=="Light drizzle")||(isday==1&&des=="Freezing drizzle")||(isday==1&&des=="Patchy rain")||(isday==0&&des=="Light Rain")){
        icon= R.drawable.dlrain
    }
    else if((isday==0&&des=="Patchy rain possible")||(isday==0&&des=="Patchy freezing drizzle possible")||(isday==0&&des=="Patchy light drizzle")||(isday==1&&des=="Light drizzle")||(isday==0&&des=="Freezing drizzle")||(isday==0&&des=="Patchy rain")||(isday==0&&des=="Light rain")){
        icon= R.drawable.nlrain_0
    }
    else if((isday==1&&des=="Patchy snow possible")||isday==1&&des=="Patchy sleet possible"||(isday==1&&des=="Blowing snow")||(isday==1&&des=="Blizzard")){
        icon= R.drawable.dsnow
    }
    else if((isday==0&&des=="Patchy snow possible")||isday==0&&des=="Patchy sleet possible"||(isday==0&&des=="Blowing snow"||(isday==0&&des=="Blizzard"))){
        icon= R.drawable.nsnow
    }
    else if(isday==1&&des=="Thundery outbreaks possible"){
        icon= R.drawable.dthunder
    }
    else if(isday==0&&des=="Thundery outbreaks possible"){
        icon= R.drawable.nthunder
    }
    else if(isday==1&&des=="Heavy freezing drizzle"||(isday==1&&des=="Moderate rain at times")||(isday==1&&des=="Moderate rain")||(isday==1&&des=="Heavy rain at times")||(isday==1&&des=="Heavy rain")||(isday==1&&des=="Moderate or heavy freezing rain")){
        icon= R.drawable.drain
    }
    else if(isday==0&&des=="Heavy freezing drizzle"||(isday==0&&des=="Moderate rain at times")||(isday==1&&des=="Moderate rain")||(isday==0&&des=="Heavy rain at times")||(isday==0&&des=="Heavy rain")||(isday==0&&des=="Moderate or heavy freezing rain")){
        icon= R.drawable.nrain
    }
    return icon

}
