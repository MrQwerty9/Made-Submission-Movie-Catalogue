package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import android.database.Cursor

interface MovieTvPresenter {
    fun loadMovieHome(genre: Int?, region: String)
    fun loadTvHome(genre: Int?, region: String)
    fun loadMovie(sortBy: String, page: Int, genre: Int?, region: String)
    fun updateMoviePage(sortBy: String, page: Int, genre: Int?, region: String)
    fun loadTvShow(sortBy: String, page: Int, genre: Int?, region: String)
    fun getGenreMovie()
    fun getGenreTv()
    fun setGridLayout()
    fun findMovies(query: String)
    fun findTv(query: String)
    fun loadFavoriteProvider(): Cursor?
    fun showFavoriteProvider(cursor: Cursor, isMovie: Boolean)
}