package com.tauan.themovieapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tauan.themovieapp.data.local.dao.MovieDao
import com.tauan.themovieapp.domain.model.Movie

@Database(
    entities = [Movie::class], version = 1, exportSchema = false
)
abstract class MovieDataBase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}