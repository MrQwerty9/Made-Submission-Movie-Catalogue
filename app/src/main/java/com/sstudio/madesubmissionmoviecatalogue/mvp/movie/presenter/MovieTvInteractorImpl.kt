package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import com.sstudio.madesubmissionmoviecatalogue.data.api.MovieDbApi
import com.sstudio.madesubmissionmoviecatalogue.model.MoviesResponse
import retrofit2.Call

class MovieTvInteractorImpl(private val movieDbApi: MovieDbApi) :
    MovieTvInteractor {
    override fun getPopularMovies(key: String, language: String): Call<MoviesResponse> {
        return movieDbApi.getPopularMovies(key, language)
    }

    override fun getPopularTv(key: String, language: String): Call<MoviesResponse> {
        return movieDbApi.getPopularTv(key, language)
    }
}