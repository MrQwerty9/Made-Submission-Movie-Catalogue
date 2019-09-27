package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import com.sstudio.madesubmissionmoviecatalogue.model.MoviesResponse
import retrofit2.Call

interface MovieTvInteractor {
    fun getPopularMovies(key: String, language: String): Call<MoviesResponse>
    fun getPopularTv(key: String, language: String): Call<MoviesResponse>
    fun findTv(key: String, language: String, query: String): Call<MoviesResponse>
    fun findMovie(key: String, language: String, query: String): Call<MoviesResponse>
    fun getMovieRelease(key: String, language: String, dateGte: String, dateLte: String): Call<MoviesResponse>
}