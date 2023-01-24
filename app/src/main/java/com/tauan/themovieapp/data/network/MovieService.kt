package com.tauan.themovieapp.data.network

import com.tauan.themovieapp.data.model.image.ImageDto
import com.tauan.themovieapp.data.model.movie.MovieDto
import com.tauan.themovieapp.data.model.moviedetail.MovieDetailDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieService {

    @GET("/3/list/20")
    suspend fun getMovieList(
        @Query("api_key") apiKey: String,
    ): Response<MovieDto>

    @GET("/3/movie/{movieId}")
    suspend fun getMovieDetail(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String,
    ): Response<MovieDetailDto>

    @GET("/3/movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): Response<ImageDto>

}