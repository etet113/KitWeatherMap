package com.example.kitweathermap.repository

import com.example.kitweathermap.datasource.OpenWeatherDataSource
import com.example.kitweathermap.response.City
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

import kotlinx.coroutines.flow.map

class OpenWeatherRepository(private val defaultDispatcher: CoroutineDispatcher) {

    private val dataSource = OpenWeatherDataSource()

    suspend fun getCityState(name:String): Flow<City> {
        return flow{
            dataSource.getCityState(name).map { response ->
                if(response.isSuccessful) response.body() else null
            }.collect {
                it?.apply {
                    emit(this)
                }
            }
        }.flowOn(defaultDispatcher)
    }

}