package com.tauan.themovieapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tauan.themovieapp.data.MovieService
import com.tauan.themovieapp.data.model.image.ImageDto
import com.tauan.themovieapp.data.model.image.toPoster
import com.tauan.themovieapp.data.model.movie.MovieDto
import com.tauan.themovieapp.data.model.movie.toMovie
import com.tauan.themovieapp.data.model.moviedetail.MovieDetailDto
import com.tauan.themovieapp.data.model.moviedetail.toMovie
import com.tauan.themovieapp.model.Movie
import com.tauan.themovieapp.util.Constants
import com.tauan.themovieapp.util.DataState
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieViewModel : ViewModel() {

    private val _movieLiveData = MutableLiveData<Movie?>()
    private val _listLiveData = MutableLiveData<List<Movie>?>()
    private val _navigationToDetailsLiveData = MutableLiveData<Unit?>()
    private val _screenState = MutableLiveData<DataState>()
    private val _postersLiveData = MutableLiveData<List<CarouselItem>?>()

    private val retrofit = Retrofit.Builder().baseUrl(Constants.API_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    private val service = retrofit.create(MovieService::class.java)

    val movieLiveData: LiveData<Movie?>
        get() = _movieLiveData

    val listLiveData: LiveData<List<Movie>?>
        get() = _listLiveData

    val navigationToDetailsLiveData: LiveData<Unit?>
        get() = _navigationToDetailsLiveData

    val screenState: LiveData<DataState>
        get() = _screenState

    val postersLiveData: LiveData<List<CarouselItem>?>
        get() = _postersLiveData

    init {
        getData()
    }

    private fun getData() {
        _screenState.postValue(DataState.LOADING)
        service.getMovieList(Constants.API_KEY).enqueue(object : Callback<MovieDto> {
            override fun onResponse(call: Call<MovieDto>, response: Response<MovieDto>) {
                if (response.isSuccessful) {
                    _listLiveData.postValue(response.body()?.items?.map { it.toMovie() })
                    _screenState.postValue(DataState.SUCESS)
                }
            }

            override fun onFailure(call: Call<MovieDto>, t: Throwable) {
                _screenState.postValue(DataState.ERROR)
            }

        })
    }

    private fun getMovieDetail(movie: Movie) {
        _screenState.postValue(DataState.LOADING)
        service.getMovieDetail(movie.id, Constants.API_KEY)
            .enqueue(object : Callback<MovieDetailDto> {
                override fun onResponse(
                    call: Call<MovieDetailDto>,
                    response: Response<MovieDetailDto>
                ) {
                    if (response.isSuccessful) {
                        _movieLiveData.postValue(response.body()?.toMovie())
                        _screenState.postValue(DataState.SUCESS)
                        _navigationToDetailsLiveData.postValue(Unit)
                    }
                }

                override fun onFailure(call: Call<MovieDetailDto>, t: Throwable) {
                    _screenState.postValue(DataState.ERROR)
                    _movieLiveData.postValue(null)
                    _navigationToDetailsLiveData.postValue(null)
                }

            })
    }

    fun getMoviePosters(movie: Movie) {
        _navigationToDetailsLiveData.postValue(null)
        _screenState.postValue(DataState.LOADING)
        service.getMovieImages(movieId = movie.id, Constants.API_KEY)
            .enqueue(object : Callback<ImageDto> {
                override fun onResponse(call: Call<ImageDto>, response: Response<ImageDto>) {
                    if (response.isSuccessful) {
                        val carouselList = mutableListOf<CarouselItem>()
                        response.body()?.posters?.map { it.toPoster() }?.forEach {
                            carouselList.add(CarouselItem(it.posterPath))
                        }.also {
                            _postersLiveData.postValue(carouselList)
                        }
                        _screenState.postValue(DataState.SUCESS)
                    }
                }

                override fun onFailure(call: Call<ImageDto>, t: Throwable) {
                    _screenState.postValue(DataState.ERROR)
                    _postersLiveData.postValue(null)
                }

            })
    }

    fun onMovieSelected(position: Int) {
        val list = _listLiveData.value
        val movie = list?.get(position)
        getMovieDetail(movie!!)
    }

}