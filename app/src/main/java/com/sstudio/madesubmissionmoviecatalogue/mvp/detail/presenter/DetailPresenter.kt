package com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv

interface DetailPresenter {
    fun favoriteClick(movieTv: MovieTv, uri: Uri)
    fun loadFavoriteProvider(uri: Uri): Cursor?
    fun addFavoriteProvider(movieTv: MovieTv)
    fun removeFavorite(id: Int)
    fun removeFavoriteProvider(uri: Uri)
    fun dumpData()
    fun setFavButton(cursor: Cursor)
}