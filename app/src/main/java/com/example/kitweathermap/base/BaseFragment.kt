package com.example.kitweathermap.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.kitweathermap.extension.dataBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

abstract class BaseFragment<B : ViewDataBinding, VM : ViewModel> : Fragment()  {

    lateinit var binding:B
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "SearchResult")

    abstract fun getBindingView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): B

    abstract fun onCreateView(binding:B)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getBindingView(inflater, container, savedInstanceState)

        dataBinding(binding) {
            onCreateView(binding)
        }.apply {
            return this
        }
    }

    protected suspend fun writePreferencesDataStore(keyName: String,value:String) {
        val key = stringPreferencesKey(keyName)
        requireContext().dataStore.edit { settings ->
            settings[key] = value
        }
    }

    protected suspend fun readPreferencesDataStore(keyName: String): String? {
        val key = stringPreferencesKey(keyName)
        return requireContext().dataStore.data
            .map { preferences ->
                preferences[key]
            }.first()
    }

}