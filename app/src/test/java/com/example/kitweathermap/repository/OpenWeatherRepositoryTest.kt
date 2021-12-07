package com.example.kitweathermap.repository

import com.example.kitweathermap.datasource.OpenWeatherDataSource
import com.example.kitweathermap.response.City
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import retrofit2.Response.success

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalCoroutinesApi
class OpenWeatherRepositoryTest {

    private val coroutineDispatcher = TestCoroutineDispatcher()

    private val sampleJson = City(
        base = "stations",
        clouds = City.Clouds(all = 78),
        cod = 200,
        coord = City.Coord(lat = 22.2855, lon = 114.1577),
        dt = 1638763621,
        id = 1819729,
        main = City.Main(
            feels_like = 21.41,
            humidity = 49,
            pressure = 1020,
            temp = 21.89,
            temp_max = 23.47,
            temp_min = 18.99
        ),
        name = "Hong Kong",
        sys = City.Sys(
            country = "HK",
            id = 2035800,
            sunrise = 1638744576,
            sunset = 1638783562,
            type = 2
        ),
        timezone = 28800,
        visibility = 10000,
        weather = listOf(
            City.Weather(
                description = "broken clouds",
                icon = "04 d",
                id = 803,
                main = "Clouds"
            )
        ),
        wind = City.Wind(deg = 11, gust = 2.24, speed = 0.45)
    )

    @RelaxedMockK
    lateinit var dataSource: OpenWeatherDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getCityStateTest() = coroutineDispatcher.runBlockingTest {
        //Given
        val repository = OpenWeatherRepository(coroutineDispatcher, dataSource)
        coEvery { dataSource.getCityState("Hong Kong") } returns flow { success(sampleJson) }
        //When
        repository.getCityState("Hong Kong").collect{
            //Then
            assertEquals(sampleJson,it)
        }
    }
}
