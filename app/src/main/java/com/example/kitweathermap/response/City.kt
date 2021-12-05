package com.example.kitweathermap.response

data class City(
    val base: String?, // stations
    val clouds: Clouds?,
    val cod: Int?, // 200
    val coord: Coord?,
    val dt: Int?, // 1638414243
    val id: Int?, // 1819729
    val main: Main?,
    val name: String?, // Hong Kong
    val sys: Sys?,
    val timezone: Int?, // 28800
    val visibility: Int?, // 10000
    val weather: List<Weather?>?,
    val wind: Wind?
) {
    data class Clouds(
        val all: Int? // 6
    )

    data class Coord(
        val lat: Double?, // 22.2855
        val lon: Double? // 114.1577
    )

    data class Main(
        val feels_like: Double?, // 290.5
        val humidity: Int?, // 39
        val pressure: Int?, // 1023
        val temp: Double?, // 291.58
        val temp_max: Double?, // 292.06
        val temp_min: Double? // 290.14
    )

    data class Sys(
        val country: String?, // HK
        val id: Int?, // 2035800
        val sunrise: Int?, // 1638398819
        val sunset: Int?, // 1638437929
        val type: Int? // 2
    )

    data class Weather(
        val description: String?, // clear sky
        val icon: String?, // 01d
        val id: Int?, // 800
        val main: String? // Clear
    )

    data class Wind(
        val deg: Int?, // 34
        val gust: Double?, // 1.34
        val speed: Double? // 0.89
    )
}
