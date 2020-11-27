package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import android.database.Cursor

interface MovieTvPresenter {
    fun loadMovieHome(genre: String, oriLanguage: String)
    fun loadMovie(shortBy: String)
    fun loadTvShow(shortBy: String)
    fun init()
    fun findMovies(query: String)
    fun findTv(query: String)
    fun loadFavoriteProvider(): Cursor?
    fun showFavoriteProvider(cursor: Cursor, isMovie: Boolean)
}