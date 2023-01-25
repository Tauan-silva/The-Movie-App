package com.tauan.themovieapp.data.model.image

import com.tauan.themovieapp.domain.model.Poster
import com.tauan.themovieapp.util.Constants

data class PosterDto(
    val aspect_ratio: Double,
    val file_path: String,
    val height: Int,
    val iso_639_1: String,
    val vote_average: Double,
    val vote_count: Int,
    val width: Int
)

fun PosterDto.toPoster(): Poster {
    return Poster(posterPath = "${Constants.IMG_URL}${Constants.IMG_SIZE}${file_path}")
}
