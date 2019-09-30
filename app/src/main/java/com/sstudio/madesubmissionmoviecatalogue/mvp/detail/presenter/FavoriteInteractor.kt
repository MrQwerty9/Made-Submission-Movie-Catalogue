package com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter

import android.database.Cursor
import android.net.Uri
import androidx.room.Query
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import io.reactivex.Flowable

interface FavoriteInteractor {
    fun getFavorite(): Flowable<List<MovieTv>>
    fun getFavoriteSync(): List<MovieTv>
    fun getFavoriteById(id: Int): Flowable<List<MovieTv>>
    fun getFavoriteCursor(): Cursor
    fun getFavByIdCursor(id: Int): Cursor
    fun insertFavorite(movieTv: MovieTv): Long
    fun removeFavorite(int: Int): Int
}