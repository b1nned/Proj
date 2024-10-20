package com.example.agrichime.agrichime.model

import com.example.agrichime.agrichime.model.data.WeatherRootList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val BASE_URL ="https://api.openweathermap.org/"
const val API_KEY ="a0fd5eeabc1d5db9f2c8da0269728ea7"
interface weatherInterface {
    @GET("data/2.5/forecast?appid=$API_KEY")
    fun getWeather(@Query("lat")lat:String, @Query("lon")lon:String): Call<WeatherRootList>
}



object WeatherApi {
    val weatherInstances: weatherInterface
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherInstances =retrofit.create(weatherInterface::class.java)
    }
}