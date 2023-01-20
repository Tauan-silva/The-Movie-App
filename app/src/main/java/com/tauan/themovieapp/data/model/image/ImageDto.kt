package com.tauan.themovieapp.data.model.image

import com.tauan.themovieapp.model.MovieImage

data class ImageDto(
    val backdrops: List<Backdrop>,
    val id: Int,
    val posters: List<PosterDto>
)

fun ImageDto.toMovieImage(): MovieImage {
    return MovieImage(
        posters = posters.map { it.toPoster() }
    )
}