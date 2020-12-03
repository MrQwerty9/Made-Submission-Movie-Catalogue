package com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.google.gson.GsonBuilder
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb.Companion.CONTENT_URI
import com.sstudio.madesubmissionmoviecatalogue.helper.MappingHelper
import com.sstudio.madesubmissionmoviecatalogue.model.*
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailView
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvInteractor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import com.sstudio.madesubmissionmoviecatalogue.data.api.Translator2 as Translator2

class DetailPresenterImpl(
    private val context: Context,
    private val detailView: DetailView,
    private val movieTvInteractor: MovieTvInteractor
) : DetailPresenter {
    private var isShowFavorite = false
    private var resultDetail: Detail? = null
    private var resultCast: CastResponse? = null
    private var resultVideo: VideoResponse? = null
    private var resultSimilar: MoviesResponse? = null

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
//                detailView.showAdditionalDetails(response.body(), null, null)
                resultDetail = response.body()
                allDetailLoaded()
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
//                detailView.showAdditionalDetails(null, response.body(),  null)
                resultCast = response.body()
                allDetailLoaded()
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
//                detailView.showAdditionalDetails(null, null, response.body())
                resultVideo = response.body()
                allDetailLoaded()
            }

            override fun onFailure(call: Call<VideoResponse>, t: Throwable?) {
                detailView.toast("${context.getString(R.string.error_data)} ${t?.message}")
                detailView.broadcastIntent()
            }
        })
    }

    override fun getMovieSimilar(id: Int, isMovie: Int) {

        val call = movieTvInteractor.getMovieTvSimilar(id, isMovie)
        call.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
//                detailView.showAdditionalDetails(null, null, response.body())
                resultSimilar = response.body()
                allDetailLoaded()
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable?) {
                detailView.toast("${context.getString(R.string.error_data)} ${t?.message}")
                detailView.broadcastIntent()
            }
        })
    }

    override fun getOverviewEN(id: Int, isMovie: Int) {
        val call = movieTvInteractor.getOverViewEN(id, isMovie)
        call.enqueue(object : Callback<Detail> {
            override fun onResponse(
                call: Call<Detail>,
                response: Response<Detail>
            ) {
                response.body()?.overview?.let {
                    detailView.showOverviewEN(it)
//                    postTranslate(it)
                }
                Log.d("mytag", "response ${response.body()?.overview}")
            }

            override fun onFailure(call: Call<Detail>, t: Throwable?) {
                detailView.toast("${context.getString(R.string.error_data)} ${t?.message}")
                detailView.broadcastIntent()
            }
        })
    }

    override fun postTranslate(text: String) {
        val client = OkHttpClient()
        val translator2 = Translator2()
        client.newCall(translator2.post(text)).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d("mytag", "fail")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val body = response.body()?.string()

                val gson = GsonBuilder().create()
                detailView.showTranslate(gson.fromJson(body, TranslateResponse::class.java)[0]
                    .translations[0].text)
//                Log.d("mytag", "trans $translate")
            }

        })
    }

    private fun allDetailLoaded(){
        if (resultDetail != null && resultCast != null && resultSimilar != null && resultVideo != null){
            detailView.showAdditionalDetails(resultDetail!!,
                resultCast!!, resultVideo!!, resultSimilar!!
            )
        }
//        Log.d("myTag", "${resultDetail?.genres} ${resultCast?.cast} ${resultVideo?.video} ${resultSimilar?.movieTv}")
        Log.d("myTag", "alldetailLoaded ${resultSimilar?.movieTv}")
    }
}