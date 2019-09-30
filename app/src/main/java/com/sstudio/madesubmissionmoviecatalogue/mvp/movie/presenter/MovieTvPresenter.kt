package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import android.database.Cursor

interface MovieTvPresenter {
    fun loadMovie()
    fun loadTvShow()
    fun loadFavorite(isMovie: Boolean)
    fun dumpData()
    fun init()
    fun findMovies(query: String)
    fun findTv(query: String)
    fun loadFavoriteProvider(): Cursor?
    fun favoriteToListProvider(cursor: Cursor, isMovie: Boolean)
}