package com.example.kitweathermap.repository

import com.example.kitweathermap.SearchResultLogic

class SearchPreferencesRepository(private val searchResultLogic:SearchResultLogic){
    suspend fun writeToResultList(cityName: String){
        searchResultLogic.writeToResultList(cityName)
    }

    suspend fun getLastSearchResult():String?{
       return searchResultLogic.getLastSearchResult()
    }

    suspend fun getSearchResultList(): List<String>? {
        return searchResultLogic.getSearchResultList()
    }
}
