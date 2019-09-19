package com.sstudio.madesubmissionmoviecatalogue.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import io.reactivex.Flowable

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun getAllList(): Flowable<List<MovieTv>>

    @Query("SELECT * FROM favorite WHERE id=:id")
    fun getById(id: Int): Flowable<List<MovieTv>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movieTv: MovieTv)

    @Query("DELETE FROM favorite WHERE id=:id")
    fun delete(id: Int)
}