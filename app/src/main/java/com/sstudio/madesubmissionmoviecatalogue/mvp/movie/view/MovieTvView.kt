package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view

import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTvHome
import com.sstudio.madesubmissionmoviecatalogue.model.MoviesResponse

interface MovieTvView {
    fun showMoviesTvHome(moviesTv : List<MovieTvHome>?)
    fun showMoviesTv(moviesTv : List<MovieTv>?)
    fun failShowMoviesTv(text: String?)
    fun spanCountGridLayout(i: Int)
    fun broadcastIntent()
}