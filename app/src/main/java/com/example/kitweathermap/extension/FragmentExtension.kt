package com.example.kitweathermap.extension

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

fun <T : ViewDataBinding> Fragment.dataBinding(binding: T, action: (T) -> Unit): View {
    binding.lifecycleOwner = viewLifecycleOwner
    action(binding)
    return binding.root
}
