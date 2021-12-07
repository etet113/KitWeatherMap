package com.example.kitweathermap.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.kitweathermap.SearchResultLogic
import com.example.kitweathermap.extension.toTrimList

class SearchPreferencesRepository(private val context: Context?){
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "SearchResult")

    private val searchResultLogic:SearchResultLogic by lazy {
        SearchResultLogic(context?.dataStore)
    }

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