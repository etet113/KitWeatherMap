package com.example.kitweathermap.viewmodel

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kitweathermap.repository.OpenWeatherRepository
import com.example.kitweathermap.response.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = OpenWeatherRepository(Dispatchers.IO)
    private val _cityState = MutableLiveData<List<Pair<String,String>>?>(null)

    // The UI collects from this StateFlow to get its state updates
    val cityState: LiveData<List<Pair<String,String>>?> = _cityState

    fun getCityState(cityName:String) {
        viewModelScope.launch {
            //TODO: SaveState()
            repository.getCityState(cityName).collect { city ->
                val mapdata = listOf<Pair<String,String>>(
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