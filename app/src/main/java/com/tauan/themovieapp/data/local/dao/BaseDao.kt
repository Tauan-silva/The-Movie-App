package com.tauan.themovieapp.data.local.dao

import androidx.room.*


interface BaseDao<T> {

    @Insert
    suspend fun insert(obj: T)

    @Insert
    suspend fun insertFromList(objList: List<T>)

    @Update
    suspend fun update(obj: T)

    @Update
    suspend fun updateFromList(objList: List<T>)

    @Delete
    suspend fun delete(obj: T)

    @Delete
    suspend fun deleteFromList(objList: List<T>)
}