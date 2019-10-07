package com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter

import android.database.Cursor
import android.net.Uri
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv

interface DetailPresenter {
    fun favoriteClick(movieTv: MovieTv, uri: Uri)
    fun loadFavoriteProvider(uri: Uri): Cursor?
    fun addFavoriteProvider(movieTv: MovieTv)
    fun removeFavoriteProvider(uri: Uri)
    fun setFavButton(cursor: Cursor)
    fun getMovieDetail(id: Int, isMovie: Int)
    fun getMovieCredits(id: Int, isMovie: Int)
    fun getMovieVideo(id: Int, isMovie: Int)
}