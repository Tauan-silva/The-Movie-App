package com.tauan.themovieapp.di.module

import android.content.Context
import androidx.room.Room
import com.tauan.themovieapp.data.local.MovieDataBase
import com.tauan.themovieapp.data.local.dao.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(
        @ApplicationContext
        context: Context
    ): MovieDataBase = Room.databaseBuilder(
        context.applicationContext, MovieDataBase::class.java, "movie_db"
    ).build()

    @Provides
    fun provideMovieDao(database: MovieDataBase): MovieDao = database.movieDao()
}