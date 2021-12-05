package com.example.kitweathermap.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import com.example.kitweathermap.adapter.WeatherResultAdapter
import com.example.kitweathermap.base.BaseFragment
import com.example.kitweathermap.databinding.MainFragmentBinding
import com.example.kitweathermap.viewmodel.MainViewModel
import com.example.kitweathermap.extension.isClickRightIcon
import com.example.kitweathermap.extension.isKeyEnter

class MainFragment : BaseFragment<MainFragmentBinding, MainViewModel>() {

    private val viewModel: MainViewModel by viewModels()
    private val resultAdapter = WeatherResultAdapter()

    override fun getBindingView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): MainFragmentBinding = MainFragmentBinding.inflate(inflater, container, false)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(binding: MainFragmentBinding) {
        binding.rvWeatherResult.adapter = resultAdapter
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
        }

        viewModel.cityState.observe(viewLifecycleOwner, { cityData ->
            resultAdapter.submitList(cityData)
        })

    }
}

enum class DrawablePosition(val positionNum: Int) {
    DRAWABLE_LEFT(0),
    DRAWABLE_TOP(1),
    DRAWABLE_RIGHT(2),
    DRAWABLE_BOTTOM(3)
}