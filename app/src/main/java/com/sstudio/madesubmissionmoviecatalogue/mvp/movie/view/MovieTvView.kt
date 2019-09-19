package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view

import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv

interface MovieTvView {
    fun showMoviesTv(moviesTv : List<MovieTv>?)
    fun failShowMoviesTv(text: String?)
    fun spanCountGridLayout(i: Int)
    fun broadcastIntent()
}