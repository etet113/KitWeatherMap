package com.example.kitweathermap.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.kitweathermap.extension.dataBinding

abstract class BaseFragment<B : ViewDataBinding, VM : ViewModel> : Fragment() {
    lateinit var binding: B
    val Context.searchResultDataStore: DataStore<Preferences> by preferencesDataStore(name = "SearchResult")

    abstract fun getBindingView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): B

    abstract fun onCreateView(binding: B)

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
}
