package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv

interface MovieTvPresenter {
    fun loadMovie()
    fun loadTvShow()
    fun loadFavorite(isMovie: Boolean)
    fun dumpData()
    fun init()
}