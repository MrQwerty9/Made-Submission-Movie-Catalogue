package com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import com.sstudio.madesubmissionmoviecatalogue.FavoriteWidget
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDb.Companion.CONTENT_URI
import com.sstudio.madesubmissionmoviecatalogue.helper.MappingHelper
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailView
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvPresenter
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.movie_wrapper.*
import java.lang.ref.WeakReference
import java.util.ArrayList

class DetailPresenterImpl(
    private val context: Context,
    private val detailView: DetailView,
    private val favoriteInteractor: FavoriteInteractor
) : DetailPresenter {

    private val disposable = CompositeDisposable()
    private var isShowFavorite = false

    override fun favoriteClick(movieTv: MovieTv, uri: Uri) {
        if (isShowFavorite) {
//            removeFavorite(movieTv.id)
            removeFavoriteProvider(uri)
        } else {
//            addFavorite(movieTv)
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

    override fun removeFavorite(id: Int) {
        Completable.fromAction {
            favoriteInteractor.removeFavorite(id)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(object :
            CompletableObserver {
            override fun onComplete() {
                detailView.toast("${context.getString(R.string.favorite_removed)} ")
                widgetNotifChanged()
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                detailView.toast("${e.message}")
            }
        })
    }

    override fun removeFavoriteProvider(uri: Uri) {
        context.contentResolver.delete(uri, null, null)
    }

    override fun dumpData() {
        disposable.dispose()
    }

    private fun widgetNotifChanged(){
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val component = ComponentName(context, FavoriteWidget::class.java)
        val appWidgetId = appWidgetManager.getAppWidgetIds(component)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stack_view)
    }
}