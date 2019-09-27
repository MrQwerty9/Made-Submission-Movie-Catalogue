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

    override fun findTv(key: String, language: String, query: String): Call<MoviesResponse> {
        return movieDbApi.findTv(key, language, query)
    }

    override fun findMovie(key: String, language: String, query: String): Call<MoviesResponse> {
        return movieDbApi.findMovies(key, language, query)
    }

    override fun getMovieRelease(
        key: String,
        language: String,
        dateGte: String,
        dateLte: String
    ): Call<MoviesResponse> {
        return movieDbApi.getMovieRelease(key, language, dateGte, dateLte)
    }
}