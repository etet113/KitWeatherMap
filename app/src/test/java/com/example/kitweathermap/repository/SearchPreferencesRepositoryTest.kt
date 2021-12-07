package com.example.kitweathermap.repository

import com.example.kitweathermap.SearchResultLogic
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchPreferencesRepositoryTest {
    private val coroutineDispatcher = TestCoroutineDispatcher()

    @RelaxedMockK
    lateinit var searchResultLogic: SearchResultLogic

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun writeToResultListTest() = coroutineDispatcher.runBlockingTest {
        //Given
        val repository = SearchPreferencesRepository(searchResultLogic)

        //When
        repository.writeToResultList("Hong Kong")

        //Then
        coVerify {
            searchResultLogic.writeToResultList("Hong Kong")
        }
    }

    @Test
    fun getLastSearchResultTest() = coroutineDispatcher.runBlockingTest {
        //Given
        val repository = SearchPreferencesRepository(searchResultLogic)
        coEvery { searchResultLogic.getLastSearchResult() } returns "Test"

        //When
        val resultString = repository.getLastSearchResult()

        //Then
        Assert.assertEquals("Test", resultString)
    }

    @Test
    fun getSearchResultListTest() = coroutineDispatcher.runBlockingTest {
        //Given
        val repository = SearchPreferencesRepository(searchResultLogic)
        coEvery { searchResultLogic.getSearchResultList() } returns listOf("Hong Kong", "London")

        //When
        val resultList = repository.getSearchResultList()

        //Then
        Assert.assertEquals(listOf("Hong Kong", "London"), resultList)
    }
}
