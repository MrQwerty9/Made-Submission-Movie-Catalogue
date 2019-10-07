package com.sstudio.madesubmissionmoviecatalogue.mvp.presenter

import android.content.Context
import android.content.res.Configuration
import android.database.Cursor
import androidx.lifecycle.ViewModel
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb
import com.sstudio.madesubmissionmoviecatalogue.helper.MappingHelper
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.mvp.view.MovieTvView
import java.util.*

class MovieTvPresenterImpl() : ViewModel(), MovieTvPresenter {
    lateinit var context: Context
    lateinit var movieTvView: MovieTvView
    var movies: List<MovieTv>? = null
    var tvShow: List<MovieTv>? = null
    var moviesFavorite: List<MovieTv>? = null
    var tvShowFavorite: List<MovieTv>? = null

    constructor(
        context: Context,
        movieTvView: MovieTvView
    ) : this() {
        this.context = context
        this.movieTvView = movieTvView
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

    override fun init() {
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            movieTvView.spanCountGridLayout(1)
        } else {
            movieTvView.spanCountGridLayout(2)
        }
    }
}