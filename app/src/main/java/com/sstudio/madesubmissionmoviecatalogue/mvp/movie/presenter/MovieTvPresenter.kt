package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

interface MovieTvPresenter {
    fun loadMovie()
    fun loadTvShow()
    fun loadFavorite(isMovie: Boolean)
    fun dumpData()
    fun init()
    fun findMovies(query: String)
    fun findTv(query: String)
}