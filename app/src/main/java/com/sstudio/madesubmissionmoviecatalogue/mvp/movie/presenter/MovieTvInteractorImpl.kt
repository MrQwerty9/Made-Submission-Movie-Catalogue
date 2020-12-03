package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import android.content.Context
import com.sstudio.madesubmissionmoviecatalogue.BuildConfig
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.data.api.MovieDbApi
import com.sstudio.madesubmissionmoviecatalogue.model.*
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*

class MovieTvInteractorImpl(private val movieDbApi: MovieDbApi, context: Context) :
    MovieTvInteractor {
    private val language = context.getString(R.string.language) //Resources.getSystem().getString(R.string.language)
    private val apiKey = BuildConfig.TMDB_API_KEY
    
    override fun getNowPlayingMovies(page: Int, genre: Int?, region: String): Call<MoviesResponse> {
        val dateNow = dateWeek(0) //now
        val dateAgo = dateWeek(-6)
        return movieDbApi.getDiscoverMovies(apiKey, language, MovieTvPresenterImpl.POPULAR, page, dateAgo, dateNow, 0, "", genre?.toString() ?: "", region)
    }

    override fun getPopularMovies(page: Int, genre: Int?, region: String): Call<MoviesResponse> {
        return movieDbApi.getDiscoverMovies(apiKey, language, MovieTvPresenterImpl.POPULAR, page, "", "", 0, "", genre?.toString() ?: "", region)
    }

    override fun getTopRatedMovies(page: Int, genre: Int?, region: String): Call<MoviesResponse> {
        var voteCountGte = 0
        voteCountGte = if (genre == null) {
            200
        }else{
            10
        }
        return movieDbApi.getDiscoverMovies(apiKey, language, MovieTvPresenterImpl.TOP_RATED, page, "", "", voteCountGte, "", genre?.toString() ?: "", region)
    }

    override fun getUpcomingMovies(page: Int, genre: Int?, region: String): Call<MoviesResponse> {
        val dateAgo = dateWeek(-1)
        val dateTill = dateWeek(12) //to 3 month
        return movieDbApi.getDiscoverMovies(apiKey, language, MovieTvPresenterImpl.POPULAR, page, dateAgo, dateTill, 0, "", genre?.toString() ?: "", region)
    }

    override fun getNowPlayingMoviesTv(
        page: Int,
        genre: Int?,
        region: String
    ): Call<MoviesResponse> {
        return movieDbApi.getDiscoverMovies(apiKey, language, MovieTvPresenterImpl.POPULAR, page, "", "", 0, "", genre?.toString() ?: "", region)
    }

    override fun getPopularMoviesTv(page: Int, genre: Int?, region: String): Call<MoviesResponse> {
        return movieDbApi.getDiscoverMovies(apiKey, language, MovieTvPresenterImpl.POPULAR, page, "", "", 0, "", genre?.toString() ?: "", region)
    }

    override fun getTopRatedMoviesTv(page: Int, genre: Int?, region: String): Call<MoviesResponse> {
        return movieDbApi.getDiscoverMovies(apiKey, language, MovieTvPresenterImpl.POPULAR, page, "", "", 0, "", genre?.toString() ?: "", region)
    }

    override fun getUpcomingMoviesTv(page: Int, genre: Int?, region: String): Call<MoviesResponse> {
        return movieDbApi.getDiscoverMovies(apiKey, language, MovieTvPresenterImpl.POPULAR, page, "", "", 0, "", genre?.toString() ?: "", region)
    }

    override fun getDiscoverMovies(sortBy: String, page: Int, genre: Int?, region: String): Call<MoviesResponse> {
        return movieDbApi.getDiscoverMovies(apiKey, language, MovieTvPresenterImpl.POPULAR, page, "", "", 0, "", genre?.toString() ?: "", region)
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
        return movieDbApi.getMovieTvVideo(mIsMovie, id, apiKey, "en-US")
    }

    override fun getMovieTvSimilar(id: Int, isMovie: Int): Call<MoviesResponse> {
        val mIsMovie = if (isMovie == 1) "movie" else "tv"
        return movieDbApi.getMovieTvSimilar(mIsMovie, id, apiKey, language)
    }

    override fun getMovieGenreList(): Call<Genres> {
        return movieDbApi.getMovieGenreList(apiKey, language)
    }

    override fun getTvGenreList(): Call<Genres> {
        return movieDbApi.getTvGenreList(apiKey, language)
    }

    override fun getOverViewEN(id: Int, isMovie: Int): Call<Detail> {
        val mIsMovie = if (isMovie == 1) "movie" else "tv"
        return movieDbApi.getMovieTvDetail(mIsMovie, id, apiKey, "en-US")
    }

    private fun dateWeek(week: Int): String{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale(language))
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.WEEK_OF_YEAR, week)
        return dateFormat.format(calendar.time)
    }
}