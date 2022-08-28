package com.borabor.movieapp.data.local.dao

import androidx.room.*
import com.borabor.movieapp.data.local.entity.FavoriteTvEntity

@Dao
interface TvDao {
    @Query("SELECT * FROM favoritetventity ORDER BY date_added DESC")
    suspend fun getAllTvs(): List<FavoriteTvEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTv(tvEntity: FavoriteTvEntity)

    @Delete
    suspend fun deleteTv(tvEntity: FavoriteTvEntity)

    @Query("SELECT EXISTS (SELECT * FROM favoritetventity WHERE id=:id)")
    suspend fun tvExists(id: Int): Boolean
}