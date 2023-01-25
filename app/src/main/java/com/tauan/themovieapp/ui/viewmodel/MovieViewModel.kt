package com.tauan.themovieapp.ui.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tauan.themovieapp.data.repository.MovieRepositoryImpl
import com.tauan.themovieapp.domain.model.Movie
import com.tauan.themovieapp.util.DataState
import com.tauan.themovieapp.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    var repository: MovieRepositoryImpl
) : ViewModel() {

    private val _movieLiveData = MutableLiveData<Movie?>()
    private val _listLiveData = MutableLiveData<List<Movie>?>()
    private val _navigationToDetailsLiveData = MutableLiveData<Event<Unit>>()
    private val _screenState = MutableLiveData<DataState>()
    private val _postersLiveData = MutableLiveData<List<CarouselItem>?>()

    val movieLiveData: LiveData<Movie?>
        get() = _movieLiveData

    val listLiveData: LiveData<List<Movie>?>
        get() = _listLiveData

    val navigationToDetailsLiveData: LiveData<Event<Unit>>
        get() = _navigationToDetailsLiveData

    val screenState: LiveData<DataState>
        get() = _screenState

    val postersLiveData: LiveData<List<CarouselItem>?>
        get() = _postersLiveData

    init {
        getData()
    }

    @VisibleForTesting
    fun getData() {
        _screenState.postValue(DataState.LOADING)
        viewModelScope.launch {
            val result = repository.getMovieData()

            result.fold(
                onSuccess = {
                    it?.let {
                        _listLiveData.postValue(it)
                    }
                    _screenState.postValue(DataState.SUCESS)

                },
                onFailure = {
                    _screenState.postValue(DataState.ERROR)
                },
            )

        }
    }

    private fun getMovieDetail(movie: Movie) {
        _screenState.postValue(DataState.LOADING)
        viewModelScope.launch {
            val result = repository.getMovieDetail(movie)

            result.fold(
                onSuccess = {
                    _movieLiveData.postValue(it)
                    _screenState.postValue(DataState.SUCESS)
                    _navigationToDetailsLiveData.postValue(Event(Unit))

                },
                onFailure = {
                    _screenState.postValue(DataState.ERROR)
                    _movieLiveData.postValue(null)
                },
            )
        }

    }

    fun getMoviePosters(movie: Movie) {
        _screenState.postValue(DataState.LOADING)

        viewModelScope.launch {

            val result = repository.getMoviePosters(movie)

            result.fold(
                onSuccess = {
                    val carouselList = mutableListOf<CarouselItem>()
                    it?.let {
                        it.forEach { poster ->
                            carouselList.add(CarouselItem(poster.posterPath))
                        }.also {
                            _postersLiveData.postValue(carouselList)
                        }
                        _screenState.postValue(DataState.SUCESS)
                    }
                },
                onFailure = {
                    _screenState.postValue(DataState.ERROR)
                    _postersLiveData.postValue(null)
                },
            )
        }

    }

    fun onMovieSelected(position: Int) {
        val list = _listLiveData.value
        val movie = list?.get(position)
        movie?.let {
            getMovieDetail(it)
        }

    }
}