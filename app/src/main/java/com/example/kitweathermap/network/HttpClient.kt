package com.example.kitweathermap.network

import com.example.kitweathermap.response.City
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HttpClient {

    @GET("data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Response<City>
}
