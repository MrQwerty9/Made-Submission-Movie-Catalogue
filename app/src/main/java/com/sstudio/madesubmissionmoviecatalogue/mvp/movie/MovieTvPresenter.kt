package com.sstudio.madesubmissionmoviecatalogue.mvp.movie

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.sstudio.madesubmissionmoviecatalogue.BuildConfig
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.api.Client
import com.sstudio.madesubmissionmoviecatalogue.model.MoviesResponse
import com.sstudio.madesubmissionmoviecatalogue.api.Service
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieTvPresenter() : ViewModel() {
    lateinit var context: Context
    lateinit var movieTvView: MovieTvView
    var movies: List<MovieTv>? = null
    var tvShow: List<MovieTv>? = null

    constructor(context: Context, movieTvView: MovieTvView) : this() {
        this.context = context
        this.movieTvView = movieTvView
    }

    fun loadMovie() {
        val apiService = Client.getClient().create(Service::class.java)
        val call = apiService.getPopularMovies(BuildConfig.TMDB_API_KEY, context.getString(R.string.language))
        call.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                movies = response.body()?.movieTv
                movieTvView.showMoviesTv(movies)
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable?) {
                Toast.makeText(context, "Error Fetching Data!", Toast.LENGTH_SHORT).show()
                movieTvView.broadcastIntent()
            }
        })
    }

    fun loadTvShow() {
        val apiService = Client.getClient().create(Service::class.java)
        val call = apiService.getPopularTv(BuildConfig.TMDB_API_KEY, context.getString(R.string.language))
        call.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                tvShow = response.body()?.movieTv
                movieTvView.showMoviesTv(tvShow)
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable?) {
                Toast.makeText(context, "Error Fetching Data!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun init() {
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            movieTvView.spanCountGridLayout(1)
        } else {
            movieTvView.spanCountGridLayout(2)
        }
    }
}