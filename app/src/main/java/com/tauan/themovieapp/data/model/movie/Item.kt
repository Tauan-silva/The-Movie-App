package com.tauan.themovieapp.data.model.movie

import com.tauan.themovieapp.model.Movie
import com.tauan.themovieapp.util.Constants

data class Item(
    val adult: Boolean,
    val backdrop_path: Any,
    val genre_ids: List<Int>,
    val id: Int,
    val media_type: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

fun Item.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        description = overview,
        image = "${Constants.IMG_URL}${Constants.IMG_SIZE}${poster_path}",
        rating = "$vote_average%",
        dateRelease = release_date,
    )
}
