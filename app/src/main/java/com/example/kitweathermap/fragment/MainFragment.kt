package com.example.kitweathermap.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.kitweathermap.SearchResultLogic
import com.example.kitweathermap.adapter.SearchResultAdapter
import com.example.kitweathermap.adapter.WeatherResultAdapter
import com.example.kitweathermap.base.BaseFragment
import com.example.kitweathermap.databinding.MainFragmentBinding
import com.example.kitweathermap.viewmodel.MainViewModel
import com.example.kitweathermap.extension.isClickRightIcon
import com.example.kitweathermap.extension.isKeyEnter
import com.example.kitweathermap.repository.OpenWeatherRepository
import com.example.kitweathermap.repository.SearchPreferencesRepository
import kotlinx.coroutines.Dispatchers

class MainFragment : BaseFragment<MainFragmentBinding, MainViewModel>() {

    lateinit var viewModel: MainViewModel
    private val resultAdapter = WeatherResultAdapter()
    private val searchHintsAdapter = SearchResultAdapter()

    override fun getBindingView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): MainFragmentBinding = MainFragmentBinding.inflate(inflater, container, false)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(binding: MainFragmentBinding) {
        viewModel = ViewModelProvider(
            this,
            MainViewModel.MainViewModelFactory(
                OpenWeatherRepository(Dispatchers.IO),
                SearchPreferencesRepository(
                    SearchResultLogic(requireContext().searchResultDataStore)
                )
            )
        ).get(MainViewModel::class.java)

        binding.rvWeatherResult.adapter = resultAdapter
        binding.rvSearchResult.adapter = searchHintsAdapter

        binding.etSearch.apply {
            setOnKeyListener { view, keyCode, event ->
                when (view) {
                    is EditText -> {
                        isKeyEnter(keyCode, event) {
                            viewModel.getCityState(it)
                        }
                    }
                    else -> return@setOnKeyListener false
                }
            }
            setOnTouchListener { view, event ->
                when (view) {
                    is EditText -> {
                        isClickRightIcon(event) {
                            viewModel.getCityState(it)
                        }
                    }
                    else -> return@setOnTouchListener false
                }
            }
            doAfterTextChanged {
                viewModel.getSearchResultList()
            }
        }

        viewModel.cityState.observe(viewLifecycleOwner) { cityData ->
            resultAdapter.submitList(cityData)
        }
        viewModel.searchResultList.observe(viewLifecycleOwner) {
            searchHintsAdapter.submitList(it)
        }

        viewModel.isShowSearchHint.observe(viewLifecycleOwner) {
            if (it) {
                binding.rvWeatherResult.visibility = View.GONE
                binding.rvSearchResult.visibility = View.VISIBLE
            } else {
                binding.rvWeatherResult.visibility = View.VISIBLE
                binding.rvSearchResult.visibility = View.GONE
            }
        }

        //Auto Loading
        viewModel.detectSearchResult()
    }
}

enum class DrawablePosition(val positionNum: Int) {
    DRAWABLE_LEFT(0),
    DRAWABLE_TOP(1),
    DRAWABLE_RIGHT(2),
    DRAWABLE_BOTTOM(3)
}
