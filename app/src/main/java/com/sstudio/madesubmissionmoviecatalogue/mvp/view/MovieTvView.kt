package com.sstudio.madesubmissionmoviecatalogue.mvp.view

import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv

interface MovieTvView {
    fun showMoviesTv(moviesTv : List<MovieTv>?)
    fun failShowMoviesTv(text: String?)
    fun spanCountGridLayout(i: Int)
}