package com.example.kitweathermap.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kitweathermap.repository.OpenWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val repository:OpenWeatherRepository = OpenWeatherRepository(Dispatchers.IO)) : ViewModel() {

    private val _cityState = MutableLiveData<List<Pair<String,String?>>>()

    // The UI collects from this StateFlow to get its state updates
    val cityState: LiveData<List<Pair<String,String?>>> = _cityState

    fun getCityState(cityName:String) {
        viewModelScope.launch {
            repository.getCityState(cityName).collect { city ->
                val mapdata = listOf(
                    "City Name:" to city.name,
                    "Temperature:" to city.main?.temp.toString()+ " \u2103",
                    "Humidity:" to city.main?.humidity.toString()+ "%",
                    "Minimum temperature:" to city.main?.temp_min.toString()+" \u2103",
                    "Maximum temperature" to city.main?.temp_max.toString()+" \u2103",
                    "Wind speed" to city.wind?.speed.toString() + " m/s",
                )
                _cityState.postValue(mapdata)
            }
        }
    }

}