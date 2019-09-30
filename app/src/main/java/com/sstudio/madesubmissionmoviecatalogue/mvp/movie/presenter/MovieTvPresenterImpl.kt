package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import android.content.Context
import android.content.res.Configuration
import android.database.Cursor
import android.util.Log
import androidx.lifecycle.ViewModel
import com.sstudio.madesubmissionmoviecatalogue.BuildConfig
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb
import com.sstudio.madesubmissionmoviecatalogue.helper.MappingHelper
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.model.MoviesResponse
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.FavoriteInteractor
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
    private lateinit var favoriteInteractor: FavoriteInteractor
    private val disposable = CompositeDisposable()
    var movies: List<MovieTv>? = null
    var tvShow: List<MovieTv>? = null
    var moviesFavorite: List<MovieTv>? = null
    var tvShowFavorite: List<MovieTv>? = null

    constructor(
        context: Context,
        movieTvView: MovieTvView,
        movieTvInteractor: MovieTvInteractor,
        favoriteInteractor: FavoriteInteractor
    ) : this() {
        this.context = context
        this.movieTvView = movieTvView
        this.movieTvInteractor = movieTvInteractor
        this.favoriteInteractor = favoriteInteractor
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

    override fun loadFavoriteProvider(): Cursor? {
        return context.contentResolver.query(FavoriteDb.CONTENT_URI, null, null, null, null)
    }

    override fun favoriteToListProvider(cursor: Cursor, isMovie: Boolean) {
        val listNotes = MappingHelper.mapCursorToArrayList(cursor)
        val list = ArrayList<MovieTv>()
        for (movieTv in listNotes) {
            if (isMovie && movieTv.isMovie == 1) {
                list.add(movieTv)
            } else if (!isMovie && movieTv.isMovie == 0) {
                list.add(movieTv)
            }
        }
        movieTvView.showMoviesTv(list)
    }

    override fun loadFavorite(isMovie: Boolean) {

        disposable.add(
            favoriteInteractor.getFavorite()
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

    override fun findMovies(query: String) {
        val call = movieTvInteractor.findMovie(
            BuildConfig.TMDB_API_KEY,
            context.getString(R.string.language),
            query
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

    override fun findTv(query: String) {
        val call = movieTvInteractor.findTv(
            BuildConfig.TMDB_API_KEY,
            context.getString(R.string.language),
            query
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