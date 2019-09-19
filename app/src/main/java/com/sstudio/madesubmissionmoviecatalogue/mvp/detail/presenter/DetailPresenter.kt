package com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter

import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv

interface DetailPresenter {
    fun loadFavorite(id: Int)
    fun addFavorite(movieTv: MovieTv)
    fun removeFavorite(id: Int)
    fun dumpData()
}