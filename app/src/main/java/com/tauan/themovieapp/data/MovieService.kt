package com.tauan.themovieapp.data

import com.tauan.themovieapp.data.model.image.ImageDto
import com.tauan.themovieapp.data.model.moviedetail.MovieDetailDto
import com.tauan.themovieapp.data.model.movie.MovieDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieService {

    @GET("/3/list/20")
    fun getMovieList(
        @Query("api_key") apiKey: String,
    ): Call<MovieDto>

    @GET("/3/movie/{movieId}")
    fun getMovieDetail(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String,
    ): Call<MovieDetailDto>

    @GET("/3/movie/{movie_id}/images")
    fun getMovieImages(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): Call<ImageDto>

}