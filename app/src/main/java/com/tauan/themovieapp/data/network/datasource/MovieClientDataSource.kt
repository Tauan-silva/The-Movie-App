package com.tauan.themovieapp.data.network.datasource

import com.tauan.themovieapp.data.model.image.toPoster
import com.tauan.themovieapp.data.model.movie.toMovie
import com.tauan.themovieapp.data.model.moviedetail.toMovie
import com.tauan.themovieapp.data.network.MovieService
import com.tauan.themovieapp.domain.datasource.MovieDataSource
import com.tauan.themovieapp.domain.model.Movie
import com.tauan.themovieapp.domain.model.Poster
import com.tauan.themovieapp.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieClientDataSource @Inject constructor() : MovieDataSource {

    @Inject
    lateinit var service: MovieService

    override suspend fun getMovieData(): Result<List<Movie>?> = withContext(Dispatchers.IO) {

        val response = service.getMovieList(Constants.API_KEY)

        when {
            response.isSuccessful -> Result.success(response.body()?.items?.map { it.toMovie() })
            else -> Result.failure(Throwable(response.message()))
        }
    }

    override suspend fun getMovieDetails(movie: Movie): Result<Movie?> =
        withContext(Dispatchers.IO) {

            val response = service.getMovieDetail(movieId = movie.id, Constants.API_KEY)

            when {
                response.isSuccessful -> Result.success(
                    response.body()?.toMovie()
                )
                else -> Result.failure(Throwable(response.message()))
            }
        }

    override suspend fun getMoviePosters(movie: Movie): Result<List<Poster>?> =
        withContext(Dispatchers.IO) {
            val response = service.getMovieImages(movieId = movie.id, Constants.API_KEY)

            when {
                response.isSuccessful -> Result.success(response.body()?.posters?.map { it.toPoster() })
                else -> Result.failure(Throwable(response.message()))
            }
        }

    override suspend fun saveLocalData(movieList: List<Movie>?) {
        //NO-OP
    }

    override suspend fun clearLocalData() {
        //NO-OP
    }
}