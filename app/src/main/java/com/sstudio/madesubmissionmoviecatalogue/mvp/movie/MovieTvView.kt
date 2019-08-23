package com.sstudio.madesubmissionmoviecatalogue.mvp.movie

import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv

interface MovieTvView {
    fun showMoviesTv(moviesTv : List<MovieTv>?)
    fun spanCountGridLayout(i: Int)
    fun broadcastIntent()
}