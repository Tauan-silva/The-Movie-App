package com.tauan.themovieapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tauan.themovieapp.data.local.dao.MovieDao
import com.tauan.themovieapp.model.Movie

@Database(
    entities = [Movie::class], version = 1, exportSchema = false
)
abstract class MovieDataBase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDataBase? = null

        fun getDataBase(context: Context): MovieDataBase {
            return INSTANCE ?: synchronized(this) {
                val dataBase = Room.databaseBuilder(
                    context.applicationContext, MovieDataBase::class.java, "movie_db"
                ).build()
                this.INSTANCE = dataBase
                return dataBase
            }
        }
    }
}