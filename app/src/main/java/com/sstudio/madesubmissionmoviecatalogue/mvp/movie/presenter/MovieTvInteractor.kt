package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import com.sstudio.madesubmissionmoviecatalogue.model.CastResponse
import com.sstudio.madesubmissionmoviecatalogue.model.Detail
import com.sstudio.madesubmissionmoviecatalogue.model.MoviesResponse
import com.sstudio.madesubmissionmoviecatalogue.model.VideoResponse
import retrofit2.Call

interface MovieTvInteractor {
    fun getPopularMovies(language: String): Call<MoviesResponse>
    fun getPopularTv(language: String): Call<MoviesResponse>
    fun findTv(language: String, query: String): Call<MoviesResponse>
    fun findMovie(language: String, query: String): Call<MoviesResponse>
    fun getMovieRelease(language: String, dateGte: String, dateLte: String): Call<MoviesResponse>
    fun getMovieTvDetail(id: Int, isMovie: Int, language: String): Call<Detail>
    fun getMovieTvCredits(id: Int, isMovie: Int, language: String): Call<CastResponse>
    fun getMovieTvVideo(id: Int, isMovie: Int, language: String): Call<VideoResponse>
}