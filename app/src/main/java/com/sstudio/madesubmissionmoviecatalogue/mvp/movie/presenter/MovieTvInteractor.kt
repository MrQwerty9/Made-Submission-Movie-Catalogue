package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import com.sstudio.madesubmissionmoviecatalogue.model.*
import retrofit2.Call

interface MovieTvInteractor {
    fun getNowPlayingMovies(page: Int, oriLanguage: String): Call<MoviesResponse>
    fun getPopularMovies(page: Int, genre: String, oriLanguage: String): Call<MoviesResponse>
    fun getTopRatedMovies(page: Int, genre: String, oriLanguage: String): Call<MoviesResponse>
    fun getUpcomingMovies(page: Int, oriLanguage: String): Call<MoviesResponse>
    fun getDiscoverMovies(sortBy: String, page: Int, genre: String, oriLanguage: String): Call<MoviesResponse>
    fun getPopularTv(): Call<MoviesResponse>
    fun findTv(query: String): Call<MoviesResponse>
    fun findMovie(query: String): Call<MoviesResponse>
    fun getMovieRelease(dateGte: String, dateLte: String): Call<MoviesResponse>
    fun getMovieTvDetail(id: Int, isMovie: Int, ): Call<Detail>
    fun getMovieTvCredits(id: Int, isMovie: Int, ): Call<CastResponse>
    fun getMovieTvVideo(id: Int, isMovie: Int, ): Call<VideoResponse>
    fun getMovieGenreList(): Call<Genres>
    fun getTvGenreList(): Call<Genres>
}