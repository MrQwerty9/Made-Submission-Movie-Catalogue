package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import com.sstudio.madesubmissionmoviecatalogue.BuildConfig
import com.sstudio.madesubmissionmoviecatalogue.data.api.MovieDbApi
import com.sstudio.madesubmissionmoviecatalogue.model.CastResponse
import com.sstudio.madesubmissionmoviecatalogue.model.Detail
import com.sstudio.madesubmissionmoviecatalogue.model.MoviesResponse
import com.sstudio.madesubmissionmoviecatalogue.model.VideoResponse
import retrofit2.Call

class MovieTvInteractorImpl(private val movieDbApi: MovieDbApi) :
    MovieTvInteractor {
    override fun getPopularMovies(language: String): Call<MoviesResponse> {
        return movieDbApi.getPopularMovies(BuildConfig.TMDB_API_KEY, language)
    }

    override fun getPopularTv(language: String): Call<MoviesResponse> {
        return movieDbApi.getPopularTv(BuildConfig.TMDB_API_KEY, language)
    }

    override fun findTv(language: String, query: String): Call<MoviesResponse> {
        return movieDbApi.findTv(BuildConfig.TMDB_API_KEY, language, query)
    }

    override fun findMovie(language: String, query: String): Call<MoviesResponse> {
        return movieDbApi.findMovies(BuildConfig.TMDB_API_KEY, language, query)
    }

    override fun getMovieRelease(language: String, dateGte: String, dateLte: String): Call<MoviesResponse> {
        return movieDbApi.getMovieRelease(BuildConfig.TMDB_API_KEY, language, dateGte, dateLte)
    }

    override fun getMovieTvDetail(id: Int, isMovie: Int, language: String): Call<Detail> {
        val mIsMovie = if (isMovie == 1) "movie" else "tv"
        return movieDbApi.getMovieTvDetail(mIsMovie, id, BuildConfig.TMDB_API_KEY, language)
    }

    override fun getMovieTvCredits(id: Int, isMovie: Int, language: String): Call<CastResponse> {
        val mIsMovie = if (isMovie == 1) "movie" else "tv"
        return movieDbApi.getMovieTvCredits(mIsMovie, id, BuildConfig.TMDB_API_KEY, language)
    }

    override fun getMovieTvVideo(id: Int, isMovie: Int, language: String): Call<VideoResponse> {
        val mIsMovie = if (isMovie == 1) "movie" else "tv"
        return movieDbApi.getMovieTvVideo(mIsMovie, id, BuildConfig.TMDB_API_KEY, language)
    }
}