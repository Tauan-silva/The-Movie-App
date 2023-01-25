package com.tauan.themovieapp.domain.datasource

import com.tauan.themovieapp.domain.model.Movie
import com.tauan.themovieapp.domain.model.Poster

interface MovieDataSource {

    suspend fun getMovieData(): Result<List<Movie>?>
    suspend fun getMovieDetails(movie: Movie): Result<Movie?>
    suspend fun getMoviePosters(movie: Movie): Result<List<Poster>?>
    suspend fun saveLocalData(movieList: List<Movie>?)
    suspend fun clearLocalData()
}