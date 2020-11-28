package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import android.database.Cursor

interface MovieTvPresenter {
    fun loadMovieHome(genre: Int?, region: String)
    fun loadMovie(shortBy: String, page: Int, genre: Int?, region: String)
    fun loadTvShow(shortBy: String)
    fun getGenreMovie()
    fun getGenreTv()
    fun init()
    fun findMovies(query: String)
    fun findTv(query: String)
    fun loadFavoriteProvider(): Cursor?
    fun showFavoriteProvider(cursor: Cursor, isMovie: Boolean)
}