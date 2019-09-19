package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import com.sstudio.madesubmissionmoviecatalogue.model.MoviesResponse
import retrofit2.Call

interface MovieTvInteractor {
    fun getPopularMovies(key: String, language: String): Call<MoviesResponse>
    fun getPopularTv(key: String, language: String): Call<MoviesResponse>
}