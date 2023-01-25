package com.tauan.themovieapp.data.repository

import android.content.Context
import com.tauan.themovieapp.data.local.datasource.MovieDataBaseDataSource
import com.tauan.themovieapp.data.network.datasource.MovieClientDataSource
import com.tauan.themovieapp.domain.model.Movie
import com.tauan.themovieapp.domain.model.Poster
import com.tauan.themovieapp.domain.repository.MovieRepository

class MovieRepositoryImpl(context: Context) : MovieRepository {

    private val movieClientDataSource = MovieClientDataSource()
    private val movieDataBaseDataSource = MovieDataBaseDataSource(context)

    override suspend fun getMovieData(): Result<List<Movie>?> {

        return try {
            val result = movieClientDataSource.getMovieData()

            if (result.isSuccess) {
                persistData(result.getOrNull())
                result
            } else {
                getLocalData()
            }

        } catch (e: Exception) {
            getLocalData()
        }

    }

    private suspend fun getLocalData(): Result<List<Movie>?> =
        movieDataBaseDataSource.getMovieData()

    private suspend fun persistData(movieList: List<Movie>?) {
        movieDataBaseDataSource.clearLocalData()
        movieList?.let {
            movieDataBaseDataSource.saveLocalData(it)
        }
    }

    override suspend fun saveLocalData(movieList: List<Movie>?) {
        movieList?.let {
            movieDataBaseDataSource.saveLocalData(it)
        }
    }

    override suspend fun clearLocalData() {
        movieDataBaseDataSource.clearLocalData()
    }

    override suspend fun getMovieDetail(movie: Movie): Result<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getMoviePosters(movie: Movie): Result<List<Poster>> {
        TODO("Not yet implemented")
    }

}