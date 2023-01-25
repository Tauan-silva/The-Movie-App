package com.tauan.themovieapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tauan.themovieapp.util.Constants

@Entity(tableName = Constants.MOVIE_TABLE)
data class Movie(
    @PrimaryKey(false)
    val id: Int,
    val title: String,
    val description: String,
    val image: String,
    val rating: String,
    val dateRelease: String,
)
