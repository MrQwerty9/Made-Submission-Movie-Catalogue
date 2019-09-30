package com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter

import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv

interface DetailPresenter {
    fun favoriteClick(movieTv: MovieTv)
    fun loadFavorite(id: Int)
    fun loadFavoriteProvider(id: Int, uri: Uri): Cursor?
    fun addFavorite(movieTv: MovieTv)
    fun removeFavorite(id: Int)
    fun dumpData()
    fun setFavButton(cursor: Cursor)
}