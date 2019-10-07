package com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter

import android.database.Cursor
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import io.reactivex.Flowable

interface FavoriteInteractor {
    fun getFavorite(): Flowable<List<MovieTv>>
    fun getFavoriteSync(): List<MovieTv>
    fun getFavoriteById(id: Int): Flowable<List<MovieTv>>
    fun getFavoriteCursor(): Cursor
    fun getFavByIdCursor(id: Int): Cursor
    fun insertFavorite(movieTv: MovieTv): Long
    fun removeFavorite(id: Int): Int
}