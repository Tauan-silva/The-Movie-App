package com.tauan.themovieapp.repository

import com.google.common.truth.Truth.assertThat
import com.tauan.themovieapp.data.local.datasource.MovieDataBaseDataSource
import com.tauan.themovieapp.data.network.datasource.MovieClientDataSource
import com.tauan.themovieapp.data.repository.MovieRepositoryImpl
import com.tauan.themovieapp.domain.model.Movie
import com.tauan.themovieapp.domain.model.Poster
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieRepositoryImplTest {
    @get:Rule
    val mocckRule = MockKRule(this)

    private val movieClientDataSource = mockk<MovieClientDataSource>()
    private val movieDataBaseDataSource = mockk<MovieDataBaseDataSource>()

    private val repository = MovieRepositoryImpl(movieClientDataSource, movieDataBaseDataSource)

    private val movie = mockk<Movie>()
    private val movieList = listOf(movie)
    private val poster = mockk<Poster>()
    private val posterList = listOf(poster)


    @Before
    fun init() {
        coEvery { movieDataBaseDataSource.clearLocalData() } returns Unit
        coEvery { movieDataBaseDataSource.saveLocalData(any()) } returns Unit
    }

    @Test
    fun getMovieData_whenApiSourceHadSuccess_shouldPersistDataAndReturnList() = runBlocking {

        //preparation
        val apiResponse = Result.success(movieList)
        coEvery { movieClientDataSource.getMovieData() } returns apiResponse

        //execution
        val result = repository.getMovieData()

        //validation
        assertThat(result).isEqualTo(apiResponse)
        coVerifySequence {
            movieDataBaseDataSource.clearLocalData()
            movieDataBaseDataSource.saveLocalData(movieList)
        }
    }

    @Test
    fun getMovieData_whenApiSourceHadFailed_shouldLoadLocalData() = runBlocking {

        //preparation
        val apiResponse = Result.failure<List<Movie>>(Throwable("Test"))
        val dbResponse = Result.success(movieList)
        coEvery { movieClientDataSource.getMovieData() } returns apiResponse
        coEvery { movieDataBaseDataSource.getMovieData() } returns dbResponse

        //execution
        val result = repository.getMovieData()

        //validation
        coVerify(exactly = 0) {
            movieDataBaseDataSource.clearLocalData()
            movieDataBaseDataSource.saveLocalData(any())
        }

        coVerify(exactly = 1) {
            movieDataBaseDataSource.getMovieData()
        }

        assertThat(result).isEqualTo(dbResponse)

    }

    @Test
    fun getMovieData_whenApiSourceThrowsException_shouldReturnLocalData() = runBlocking {
        //preparation
        val dbResponse = Result.success(movieList)
        coEvery { movieClientDataSource.getMovieData() } throws Exception("Test")
        coEvery { movieDataBaseDataSource.getMovieData() } returns dbResponse

        //execution
        val result = repository.getMovieData()

        //validation
        coVerify(exactly = 0) {
            movieDataBaseDataSource.clearLocalData()
            movieDataBaseDataSource.saveLocalData(any())
        }

        coVerify(exactly = 1) {
            movieDataBaseDataSource.getMovieData()
        }

        assertThat(result).isEqualTo(dbResponse)
    }

    @Test
    fun getMovieDetail_whenApiSourceHadSuccess_shouldReturnMovieDetail() = runBlocking {
        //preparation
        val apiResponse = Result.success(movie)
        coEvery { movieClientDataSource.getMovieDetails(movie) } returns apiResponse

        //execution
        val result = repository.getMovieDetail(movie)

        //validation
        assertThat(result).isEqualTo(apiResponse)
    }

    @Test
    fun getMovieDetail_whenApiSourceHadError_shouldReturnThrowable() = runBlocking {
        //preparation
        val apiResponse = Result.failure<Movie>(Throwable("Test"))
        coEvery { movieClientDataSource.getMovieDetails(movie) } returns apiResponse

        //execution
        val result = repository.getMovieDetail(movie)

        //validation
        assertThat(result).isEqualTo(apiResponse)
    }

    @Test
    fun getMoviePosters_whenApiSourceHadSuccess_shouldReturnsPostersList() = runBlocking {
        //preparation
        val apiResponse = Result.success(posterList)
        coEvery { movieClientDataSource.getMoviePosters(movie) } returns apiResponse

        //execution
        val result = repository.getMoviePosters(movie)

        //validation
        assertThat(result).isEqualTo(apiResponse)
    }

    @Test
    fun getMoviePosters_whenApiSourceHadFailed_shouldReturnsThrowable() = runBlocking {

        //preparation
        val apiResponse = Result.failure<List<Poster>>(Throwable("Test"))
        coEvery { movieClientDataSource.getMoviePosters(movie) } returns apiResponse

        //execution
        val result = repository.getMoviePosters(movie)

        //validation
        assertThat(result).isEqualTo(apiResponse)
    }

}