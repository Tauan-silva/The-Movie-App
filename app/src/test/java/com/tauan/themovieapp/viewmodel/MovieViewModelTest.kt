package com.tauan.themovieapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.tauan.themovieapp.data.repository.MovieRepositoryImpl
import com.tauan.themovieapp.domain.model.Movie
import com.tauan.themovieapp.ui.viewmodel.MovieViewModel
import com.tauan.themovieapp.util.DataState
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val dispatcher = UnconfinedTestDispatcher()

    private val repository = mockk<MovieRepositoryImpl>()
    lateinit var viewModel: MovieViewModel

    private val screenStateObserver: Observer<DataState> = mockk(relaxed = true)
    private val screenStateValues = mutableListOf<DataState>()

    private val movie = mockk<Movie>()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this, relaxed = true)

        justRun { screenStateObserver.onChanged(capture(screenStateValues)) }

        coEvery { repository.getMovieData() } returns Result.failure(Throwable("Test"))
        viewModel = MovieViewModel(repository)
        viewModel.screenState.observeForever(screenStateObserver)
        screenStateValues.clear()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.screenState.removeObserver(screenStateObserver)
        screenStateValues.clear()
    }

    @Test
    fun getData_whenRepository_hasData_shouldChangeStateToSuccess() = runBlocking {
        coEvery { repository.getMovieData() } returns Result.success(listOf(movie))

        viewModel.getData()

        assertThat(screenStateValues).isEqualTo(listOf(DataState.LOADING, DataState.SUCESS))
    }

    @Test
    fun getData_whenRepository_hasError_shouldChangeStateToError() = runBlocking {
        coEvery { repository.getMovieData() } returns Result.failure(Throwable("Test"))

        viewModel.getData()

        assertThat(screenStateValues).isEqualTo(listOf(DataState.LOADING, DataState.ERROR))
    }

    @Test
    fun getData_whenRepository_hasData_shouldEmitList() = runBlocking {
        val list = listOf(movie)
        coEvery { repository.getMovieData() } returns Result.success(list)

        viewModel.getData()

        assertThat(viewModel.listLiveData.value).isEqualTo(list)
    }

    @Test
    fun getData_whenRepository_hasError_shouldNotEmitList() = runBlocking {
        coEvery { repository.getMovieData() } returns Result.failure(Throwable("Test"))

        viewModel.getData()

        assertThat(viewModel.listLiveData.value).isNull()
    }

}