package com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter

import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb.Companion.CONTENT_URI
import com.sstudio.madesubmissionmoviecatalogue.helper.MappingHelper
import com.sstudio.madesubmissionmoviecatalogue.model.CastResponse
import com.sstudio.madesubmissionmoviecatalogue.model.Detail
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.model.VideoResponse
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailView
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvInteractor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPresenterImpl(
    private val context: Context,
    private val detailView: DetailView,
    private val movieTvInteractor: MovieTvInteractor
) : DetailPresenter {
    private var isShowFavorite = false

    override fun favoriteClick(movieTv: MovieTv, uri: Uri) {
        if (isShowFavorite) {
            removeFavoriteProvider(uri)
        } else {
            addFavoriteProvider(movieTv)
        }
    }

    override fun loadFavoriteProvider(uri: Uri): Cursor? {
        return context.contentResolver.query(uri, null, null, null, null)
    }

    override fun setFavButton(cursor: Cursor) {
        val listNotes = MappingHelper.mapCursorToArrayList(cursor)
        if (listNotes.isNotEmpty()) {
            isShowFavorite = true
            detailView.isShowFavorite(R.drawable.ic_favorite_pink_24dp)
        } else {
            isShowFavorite = false
            detailView.isShowFavorite(R.drawable.ic_favorite_white_24dp)
        }
    }

    override fun addFavoriteProvider(movieTv: MovieTv) {
        val values = MappingHelper.mapMovieTvToContentValues(movieTv)
        context.contentResolver.insert(CONTENT_URI, values)
    }

    override fun removeFavoriteProvider(uri: Uri) {
        context.contentResolver.delete(uri, null, null)
    }

    override fun getMovieDetail(id: Int, isMovie: Int) {
        val call = movieTvInteractor.getMovieTvDetail(id, isMovie)
        call.enqueue(object : Callback<Detail> {
            override fun onResponse(
                call: Call<Detail>,
                response: Response<Detail>
            ) {
                detailView.showAdditionalDetails(response.body(), null, null)
            }

            override fun onFailure(call: Call<Detail>, t: Throwable?) {
                detailView.toast("${context.getString(R.string.error_data)} ${t?.message}")
                detailView.broadcastIntent()
            }
        })
    }

    override fun getMovieCredits(id: Int, isMovie: Int) {
        val call = movieTvInteractor.getMovieTvCredits(id, isMovie)
        call.enqueue(object : Callback<CastResponse> {
            override fun onResponse(
                call: Call<CastResponse>,
                response: Response<CastResponse>
            ) {
                detailView.showAdditionalDetails(null, response.body(),  null)
            }

            override fun onFailure(call: Call<CastResponse>, t: Throwable?) {
                detailView.toast("${context.getString(R.string.error_data)} ${t?.message}")
                detailView.broadcastIntent()
            }
        })
    }

    override fun getMovieVideo(id: Int, isMovie: Int) {
        val call = movieTvInteractor.getMovieTvVideo(id, isMovie)
        call.enqueue(object : Callback<VideoResponse> {
            override fun onResponse(
                call: Call<VideoResponse>,
                response: Response<VideoResponse>
            ) {
                detailView.showAdditionalDetails(null, null, response.body())
            }

            override fun onFailure(call: Call<VideoResponse>, t: Throwable?) {
                detailView.toast("${context.getString(R.string.error_data)} ${t?.message}")
                detailView.broadcastIntent()
            }
        })
    }
}