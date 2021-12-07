package com.example.kitweathermap

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.kitweathermap.extension.toTrimList
import kotlinx.coroutines.flow.*

class SearchResultLogic(
    private val dataStore: DataStore<Preferences>?
) {
    private val resultMaxSize = 5
    private val resultListKey = stringPreferencesKey("ResultList")

    suspend fun writeToResultList(cityName: String) {
        if (readToResultList().isNullOrEmpty()) {
            dataStore?.edit { settings ->
                settings[resultListKey] = cityName
            }
        } else {
            readToResultList()?.toTrimList()?.apply {
                if (size >= resultMaxSize) {
                    removeFirst()
                }
                // TODO // Remove Same Item
                add(cityName)
                dataStore?.edit { settings ->
                    settings[resultListKey] = joinToString() // "1,2,3,4"
                }
            }
        }
    }

    suspend fun getLastSearchResult(): String? {
        return getSearchResultList()?.last()
    }

    suspend fun getSearchResultList(): List<String>? {
        return readToResultList()?.toTrimList()?.toList()
    }

    private suspend fun readToResultList(): String? {
        return dataStore?.data?.map { preferences ->
            preferences[resultListKey]
        }?.first()
    }
}
