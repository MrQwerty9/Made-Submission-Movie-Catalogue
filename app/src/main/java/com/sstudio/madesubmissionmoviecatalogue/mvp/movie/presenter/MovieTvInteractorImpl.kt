package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import android.content.Context
import android.content.res.Resources
import com.sstudio.madesubmissionmoviecatalogue.BuildConfig
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.data.api.MovieDbApi
import com.sstudio.madesubmissionmoviecatalogue.model.*
import retrofit2.Call

class MovieTvInteractorImpl(private val movieDbApi: MovieDbApi, context: Context) :
    MovieTvInteractor {
    private val language = context.getString(R.string.language) //Resources.getSystem().getString(R.string.language)
    private val apiKey = BuildConfig.TMDB_API_KEY
    
    override fun getNowPlayingMovies(page: Int, oriLanguage: String): Call<MoviesResponse> {
        return movieDbApi.getNowPlayingMovies(apiKey, language)
    }

    override fun getPopularMovies(page: Int, genre: String, oriLanguage: String): Call<MoviesResponse> {
        return movieDbApi.getPopularMovies(apiKey, language)
    }

    override fun getTopRatedMovies(page: Int, genre: String, oriLanguage: String): Call<MoviesResponse> {
        return movieDbApi.getTopRatedMovies(apiKey, language)
    }

    override fun getUpcomingMovies(page: Int, oriLanguage: String): Call<MoviesResponse> {
        return movieDbApi.getUpcomingMovies(apiKey, language)
    }

    override fun getDiscoverMovies(sortBy: String, page: Int, genre: String, oriLanguage: String): Call<MoviesResponse> {
        return movieDbApi.getDiscoverMovies(apiKey, language, sortBy, page, genre, oriLanguage)
    }

    override fun getPopularTv(): Call<MoviesResponse> {
        return movieDbApi.getPopularTv(apiKey, language)
    }

    override fun findTv(query: String): Call<MoviesResponse> {
        return movieDbApi.findTv(apiKey, language, query)
    }

    override fun findMovie(query: String): Call<MoviesResponse> {
        return movieDbApi.findMovies(apiKey, language, query)
    }

    override fun getMovieRelease(dateGte: String, dateLte: String): Call<MoviesResponse> {
        return movieDbApi.getMovieRelease(apiKey, language, dateGte, dateLte)
    }

    override fun getMovieTvDetail(id: Int, isMovie: Int, ): Call<Detail> {
        val mIsMovie = if (isMovie == 1) "movie" else "tv"
        return movieDbApi.getMovieTvDetail(mIsMovie, id, apiKey, language)
    }

    override fun getMovieTvCredits(id: Int, isMovie: Int, ): Call<CastResponse> {
        val mIsMovie = if (isMovie == 1) "movie" else "tv"
        return movieDbApi.getMovieTvCredits(mIsMovie, id, apiKey, language)
    }

    override fun getMovieTvVideo(id: Int, isMovie: Int, ): Call<VideoResponse> {
        val mIsMovie = if (isMovie == 1) "movie" else "tv"
        return movieDbApi.getMovieTvVideo(mIsMovie, id, apiKey, language)
    }

    override fun getMovieGenreList(): Call<Genres> {
        return movieDbApi.getMovieGenreList(apiKey, language)
    }

    override fun getTvGenreList(): Call<Genres> {
        return movieDbApi.getTvGenreList(apiKey, language)
    }
}