package com.example.kitweathermap.datasource

import com.example.kitweathermap.BuildConfig
import com.example.kitweathermap.network.APIClient
import com.example.kitweathermap.response.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class OpenWeatherDataSource {

    private val apiClient = APIClient.getAPIClient()
    private val apiKey = BuildConfig.API_KEY

    suspend fun getCityState(name:String): Flow<Response<City>> {
        return flow {
            emit(apiClient.getWeatherByCity(name,apiKey))
        }
    }

}