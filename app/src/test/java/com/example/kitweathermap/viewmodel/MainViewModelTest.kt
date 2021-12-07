package com.example.kitweathermap.viewmodel

import androidx.lifecycle.Observer
import com.example.kitweathermap.repository.OpenWeatherRepository
import com.example.kitweathermap.response.City
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.kitweathermap.repository.SearchPreferencesRepository
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*

@ExperimentalCoroutinesApi
class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val coroutineDispatcher = TestCoroutineDispatcher()

    @RelaxedMockK
    lateinit var repository: OpenWeatherRepository

    @RelaxedMockK
    lateinit var searchResultRepository: SearchPreferencesRepository

    lateinit var viewModel: MainViewModel

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
    lateinit var cityStateObserver: Observer<List<Pair<String, String?>>>

    @RelaxedMockK
    lateinit var searchResultListObserver: Observer<List<String>>

    @RelaxedMockK
    lateinit var isShowSearchHintObserver: Observer<Boolean>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(coroutineDispatcher)
        viewModel = MainViewModel(repository, searchResultRepository)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun getCityStateTest() = coroutineDispatcher.runBlockingTest {
        //Given
        val slot = slot<List<Pair<String, String>>>()
        coEvery { repository.getCityState("Hong Kong") } returns flow { emit(sampleJson) }
        viewModel.cityState.observeForever(cityStateObserver)
        //When
        viewModel.getCityState("Hong Kong")

        //Then
        verify {
            cityStateObserver.onChanged(capture(slot))
        }
        Assert.assertEquals(
            listOf(
                "City Name:" to "Hong Kong",
                "Temperature:" to "21.89 \u2103",
                "Humidity:" to "49%",
                "Minimum temperature:" to "18.99 \u2103",
                "Maximum temperature" to "23.47 \u2103",
                "Wind speed" to "0.45 m/s",
            ), slot.captured
        )
    }

    @Test
    fun detectSearchResult() {
        //Given
        val slot = slot<List<Pair<String, String>>>()
        coEvery { searchResultRepository.getLastSearchResult() } returns "Hong Kong"
        coEvery { repository.getCityState("Hong Kong") } returns flow { emit(sampleJson) }
        viewModel.cityState.observeForever(cityStateObserver)

        //When
        viewModel.detectSearchResult()

        //Then
        verify {
            cityStateObserver.onChanged(capture(slot))
        }
        Assert.assertEquals(
            listOf(
                "City Name:" to "Hong Kong",
                "Temperature:" to "21.89 \u2103",
                "Humidity:" to "49%",
                "Minimum temperature:" to "18.99 \u2103",
                "Maximum temperature" to "23.47 \u2103",
                "Wind speed" to "0.45 m/s",
            ), slot.captured
        )
    }

    @Test
    fun getSearchResultList() {
        val slot = slot<List<String>>()
        val slotHints = slot<Boolean>()
        coEvery { searchResultRepository.getSearchResultList() } returns listOf(
            "Hong Kong",
            "Tokyo",
            "London"
        )
        viewModel.searchResultList.observeForever(searchResultListObserver)
        viewModel.isShowSearchHint.observeForever(isShowSearchHintObserver)
        //when
        viewModel.getSearchResultList()

        //Then
        verify {
            searchResultListObserver.onChanged(capture(slot))
            isShowSearchHintObserver.onChanged(capture(slotHints))
        }

        Assert.assertEquals(listOf("Hong Kong", "Tokyo", "London"), slot.captured)
        Assert.assertEquals(true, slotHints.captured)
    }
}
