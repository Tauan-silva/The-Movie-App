package com.tauan.themovieapp.data.local.datasource

import android.content.Context
import com.tauan.themovieapp.data.local.MovieDataBase
import com.tauan.themovieapp.domain.datasource.MovieDataSource
import com.tauan.themovieapp.domain.model.Movie
import com.tauan.themovieapp.domain.model.Poster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieDataBaseDataSource(
    context: Context
) : MovieDataSource {

    private val dataBase = MovieDataBase.getDataBase(context)
    private val movieDao = dataBase.movieDao()

    override suspend fun getMovieData(): Result<List<Movie>?> = withContext(Dispatchers.IO) {
        Result.success(movieDao.selectAll())
    }

    override suspend fun saveLocalData(movieList: List<Movie>?) {
        withContext(Dispatchers.IO) {
            movieList?.let {
                movieDao.insertFromList(it)
            }
        }
    }

    override suspend fun clearLocalData() {
        withContext(Dispatchers.IO) {
            movieDao.deleteAll()
        }
    }

    override suspend fun getMovieDetails(movie: Movie): Result<Movie?> {
        TODO("NO-OP YET")
    }

    override suspend fun getMoviePosters(movie: Movie): Result<List<Poster>?> {
        TODO("NO-OP YET")
    }

}