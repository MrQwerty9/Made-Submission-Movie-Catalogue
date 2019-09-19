package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import com.sstudio.madesubmissionmoviecatalogue.BuildConfig
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.model.MoviesResponse
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.DetailInteractor
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view.MovieTvView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MovieTvPresenterImpl() : ViewModel(), MovieTvPresenter {
    lateinit var context: Context
    lateinit var movieTvView: MovieTvView
    private lateinit var movieTvInteractor: MovieTvInteractor
    private lateinit var detailInteractor: DetailInteractor
    private val disposable = CompositeDisposable()
    override var movies: List<MovieTv>? = null
    override var tvShow: List<MovieTv>? = null
    override var moviesFavorite: List<MovieTv>? = null
    override var tvShowFavorite: List<MovieTv>? = null

    constructor(
        context: Context,
        movieTvView: MovieTvView,
        movieTvInteractor: MovieTvInteractor,
        detailInteractor: DetailInteractor
    ) : this() {
        this.context = context
        this.movieTvView = movieTvView
        this.movieTvInteractor = movieTvInteractor
        this.detailInteractor = detailInteractor
    }

    override fun loadMovie() {
        val call = movieTvInteractor.getPopularMovies(
            BuildConfig.TMDB_API_KEY,
            context.getString(R.string.language)
        )
        call.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                movies = response.body()?.movieTv
                movieTvView.showMoviesTv(movies)
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable?) {
                movieTvView.failShowMoviesTv("${context.getString(R.string.error_data)} ${t?.message}")
                movieTvView.broadcastIntent()
            }
        })
    }

    override fun loadTvShow() {
        val call = movieTvInteractor.getPopularTv(
            BuildConfig.TMDB_API_KEY,
            context.getString(R.string.language)
        )
        call.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                tvShow = response.body()?.movieTv
                movieTvView.showMoviesTv(tvShow)
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable?) {
                movieTvView.failShowMoviesTv("${context.getString(R.string.error_data)} ${t?.message}")
                movieTvView.broadcastIntent()
            }
        })
    }

    override fun loadFavorite(isMovie: Boolean) {
        disposable.add(
            detailInteractor.getFavorite()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn {
                    movieTvView.failShowMoviesTv(it.message)
                    Collections.emptyList()
                }
                .subscribe({
                    val list = ArrayList<MovieTv>()
                    for (movieTv in it) {
                        if (isMovie && movieTv.isMovie == 1) {
                            list.add(movieTv)
                        } else if (!isMovie && movieTv.isMovie == 0) {
                            list.add(movieTv)
                        }
                    }
                    movieTvView.showMoviesTv(list)
                }, {
                    movieTvView.failShowMoviesTv("${context.getString(R.string.error_data)} ${it?.message}")
                })
        )
    }

    override fun dumpData() {
        disposable.dispose()
    }

    override fun init() {
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            movieTvView.spanCountGridLayout(1)
        } else {
            movieTvView.spanCountGridLayout(2)
        }
    }
}