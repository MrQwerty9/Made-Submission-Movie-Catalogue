package com.sstudio.madesubmissionmoviecatalogue.data.local

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import io.reactivex.Flowable

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun getFavorite(): Flowable<List<MovieTv>>

    @Query("SELECT * FROM favorite")
    fun getFavoriteSync(): List<MovieTv>

    @Query("SELECT * FROM favorite WHERE id=:id")
    fun getById(id: Int): Flowable<List<MovieTv>>

    @Query("SELECT * FROM favorite")
    fun getFavoriteCursor(): Cursor

    @Query("SELECT * FROM favorite WHERE id=:id")
    fun getFavByIdCursor(id: Int): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(movieTv: MovieTv): Long

    @Query("DELETE FROM favorite WHERE id=:id")
    fun deleteFavorite(id: Int): Int
}