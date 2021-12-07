package com.example.kitweathermap.extension

fun String.toTrimList(delimiters:String = ","):MutableList<String>{
   return split(delimiters).map { it.trim() }.toMutableList()
}
