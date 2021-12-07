package com.example.kitweathermap.viewmodel

import androidx.lifecycle.*
import com.example.kitweathermap.repository.OpenWeatherRepository
import com.example.kitweathermap.repository.SearchPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val repository:OpenWeatherRepository,private val searchPreferencesRepository: SearchPreferencesRepository) : ViewModel() {

    private val _cityState = MutableLiveData<List<Pair<String,String?>>>()
    private val _searchResultList = MutableLiveData<List<String>>()


    val cityState: LiveData<List<Pair<String,String?>>> = _cityState
    val searchResultList:LiveData<List<String>> = _searchResultList

    val isShowSearchHint = MutableLiveData(false)

    fun getCityState(cityName:String) {
        viewModelScope.launch {
            searchPreferencesRepository.writeToResultList(cityName)
            repository.getCityState(cityName).collect { city ->
                isShowSearchHint.postValue(false)
                if(city!=null){
                    listOf(
                        "City Name:" to city.name,
                        "Temperature:" to city.main?.temp.toString()+ " \u2103",
                        "Humidity:" to city.main?.humidity.toString()+ "%",
                        "Minimum temperature:" to city.main?.temp_min.toString()+" \u2103",
                        "Maximum temperature" to city.main?.temp_max.toString()+" \u2103",
                        "Wind speed" to city.wind?.speed.toString() + " m/s",
                    ).apply {
                        _cityState.postValue(this)
                    }
                }else{
                    _cityState.postValue(listOf("City Name:" to "Can't search $cityName"))
                }
            }
        }
    }

    fun detectSearchResult(){
        viewModelScope.launch {
            searchPreferencesRepository.getLastSearchResult()?.apply {
                getCityState(this)
            }
        }
    }

    fun getSearchResultList(){
        viewModelScope.launch {
            isShowSearchHint.postValue(true)
            searchPreferencesRepository.getSearchResultList()?.apply {
                if(size>0){
                    _searchResultList.postValue(this)
                }
            }
        }
    }

    class MainViewModelFactory(
        private val repository: OpenWeatherRepository,
        private val searchPreferencesRepository: SearchPreferencesRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repository, searchPreferencesRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}