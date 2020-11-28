package com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter

import android.content.Context
import android.content.res.Configuration
import android.database.Cursor
import android.util.Log
import androidx.lifecycle.ViewModel
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb
import com.sstudio.madesubmissionmoviecatalogue.helper.MappingHelper
import com.sstudio.madesubmissionmoviecatalogue.model.Genres
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTvHome
import com.sstudio.madesubmissionmoviecatalogue.model.MoviesResponse
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.FavoriteInteractor
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view.MovieTvView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class MovieTvPresenterImpl() : ViewModel(), MovieTvPresenter {
    lateinit var context: Context
    lateinit var movieTvView: MovieTvView
    private lateinit var movieTvInteractor: MovieTvInteractor
    private lateinit var favoriteInteractor: FavoriteInteractor
    var moviesHome: ArrayList<MovieTvHome>? = null
    var tvShowHome: ArrayList<MovieTvHome>? = null
    var movies: List<MovieTv>? = null
    var tvShow: List<MovieTv>? = null
    var moviesFavorite: List<MovieTv>? = null
    var tvShowFavorite: List<MovieTv>? = null
    private var movieGenreList: List<Genres.Genre>? = null
    private var tvGenreList: List<Genres.Genre>? = null

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

    companion object{
        val POPULAR = "popularity.desc"
        val NOW_PLAYING = "now_playing"
        val TOP_RATED = "vote_average.desc"
        val UPCOMING = "upcoming"
        val BY_GENRE = "genre"
        val LOCAL = "local"
        val TOTAL_SHORT_BY = 4
    }

    override fun loadMovieHome(genre: Int?, region: String) {
        moviesHome = ArrayList()
        getGenreMovie()
        repeat(TOTAL_SHORT_BY){ //initial array
            moviesHome?.add(MovieTvHome())
        }
        getNowPlaying(1, genre, region).enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                val movieTvHome = MovieTvHome()
                movieTvHome.title = context.getString(R.string.sort_now_playing)
                movieTvHome.movieTvHome = response.body()?.movieTv as ArrayList<MovieTv>
                moviesHome?.set(0, movieTvHome)
                loadedMovieHome()
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable?) {
                movieTvView.failShowMoviesTv("${context.getString(R.string.error_data)} ${t?.message}")
                movieTvView.broadcastIntent()
            }
        })

        getPopular(1, genre, region).enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                val movieTvHome = MovieTvHome()
                movieTvHome.title = context.getString(R.string.sort_popular)
                movieTvHome.movieTvHome = response.body()?.movieTv as ArrayList<MovieTv>
                moviesHome?.set(1, movieTvHome)
                loadedMovieHome()
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable?) {
                movieTvView.failShowMoviesTv("${context.getString(R.string.error_data)} ${t?.message}")
                movieTvView.broadcastIntent()
            }
        })

        getTopRated(1, genre, region).enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                val movieTvHome = MovieTvHome()
                movieTvHome.title = context.getString(R.string.sort_top_rated)
                movieTvHome.movieTvHome = response.body()?.movieTv as ArrayList<MovieTv>
                moviesHome?.set(2, movieTvHome)
                loadedMovieHome()
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable?) {
                movieTvView.failShowMoviesTv("${context.getString(R.string.error_data)} ${t?.message}")
                movieTvView.broadcastIntent()
            }
        })

        getUpComing(1, genre, region).enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                val movieTvHome = MovieTvHome()
                movieTvHome.title = context.getString(R.string.sort_upcoming)
                movieTvHome.movieTvHome = response.body()?.movieTv as ArrayList<MovieTv>
                moviesHome?.set(3, movieTvHome)
                loadedMovieHome()
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable?) {
                movieTvView.failShowMoviesTv("${context.getString(R.string.error_data)} ${t?.message}")
                movieTvView.broadcastIntent()
            }
        })
    }

    override fun loadMovie(shortBy: String, page: Int, genre: Int?, region: String) {
        val call: Call<MoviesResponse> = when (shortBy) {
            NOW_PLAYING -> {
                getNowPlaying(page, genre, region)
            }
            POPULAR -> {
                getPopular(page, genre, region)
            }
            TOP_RATED -> {
                getTopRated(page, genre, region)
            }
            else -> {
                getUpComing(page, genre, region)
            }
        }

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

    override fun loadTvShow(shortBy: String) {
        val call = movieTvInteractor.getPopularTv(
            
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

    override fun showFavoriteProvider(cursor: Cursor, isMovie: Boolean) {
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

    override fun findMovies(query: String) {
        val call = movieTvInteractor.findMovie(query)
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
        val call = movieTvInteractor.findTv(query)
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

    override fun getGenreMovie(){
        val call = movieTvInteractor.getMovieGenreList()
        call.enqueue(object : Callback<Genres> {
            override fun onResponse(
                call: Call<Genres>,
                response: Response<Genres>
            ) {
                Log.d("myTag genre", "response")
                movieTvView.showGenreList(response.body()?.genres)
            }

            override fun onFailure(call: Call<Genres>, t: Throwable?) {
                movieTvView.broadcastIntent()
                Log.d("myTag genre", "failure")
            }
        })
    }

    override fun getGenreTv(){
        val call = movieTvInteractor.getTvGenreList()
        call.enqueue(object : Callback<Genres> {
            override fun onResponse(
                call: Call<Genres>,
                response: Response<Genres>
            ) {
                movieTvView.showGenreList(response.body()?.genres)
            }

            override fun onFailure(call: Call<Genres>, t: Throwable?) {
                movieTvView.broadcastIntent()
            }
        })
    }

    override fun init() {
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            movieTvView.spanCountGridLayout(1)
        } else {
            movieTvView.spanCountGridLayout(2)
        }
    }

    private fun loadedMovieHome(){
        if (moviesHome?.size == TOTAL_SHORT_BY){
            movieTvView.showMoviesTvHome(moviesHome)
        }
    }

    private fun getDiscoverMovie(sortBy: String, page: Int, genre: Int?, region: String): Call<MoviesResponse>{
        return movieTvInteractor.getDiscoverMovies(sortBy, page, genre, region)
    }

    private fun getNowPlaying(page: Int, genre: Int?, region: String): Call<MoviesResponse>{
        return movieTvInteractor.getNowPlayingMovies(page, genre, region)
    }

    private fun getPopular(page: Int, genre: Int?, region: String): Call<MoviesResponse>{
        return movieTvInteractor.getPopularMovies(page, genre, region)
    }

    private fun getTopRated(page: Int, genre: Int?, region: String): Call<MoviesResponse>{
        return movieTvInteractor.getTopRatedMovies(page, genre, region)
    }

    private fun getUpComing(page: Int, genre: Int?, region: String): Call<MoviesResponse>{
        return movieTvInteractor.getUpcomingMovies(page, genre, region)
    }
}