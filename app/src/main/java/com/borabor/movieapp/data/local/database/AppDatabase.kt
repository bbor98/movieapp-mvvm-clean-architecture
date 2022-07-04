package com.borabor.movieapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.borabor.movieapp.data.local.dao.MovieDao
import com.borabor.movieapp.data.local.dao.TvDao
import com.borabor.movieapp.data.local.entity.FavoriteMovieEntity
import com.borabor.movieapp.data.local.entity.FavoriteTvEntity

@Database(entities = [FavoriteMovieEntity::class, FavoriteTvEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvDao(): TvDao
}