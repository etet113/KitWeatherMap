package com.example.kitweathermap

import android.widget.EditText
import androidx.databinding.InverseBindingAdapter

@InverseBindingAdapter(attribute = "android:text")
fun EditText.getBindingString(): String {
    return text.toString()
}