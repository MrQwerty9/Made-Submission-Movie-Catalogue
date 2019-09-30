package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import android.database.Cursor

interface MovieTvPresenter {
    fun loadMovie()
    fun loadTvShow()
    fun init()
    fun findMovies(query: String)
    fun findTv(query: String)
    fun loadFavoriteProvider(): Cursor?
    fun showFavoriteProvider(cursor: Cursor, isMovie: Boolean)
}