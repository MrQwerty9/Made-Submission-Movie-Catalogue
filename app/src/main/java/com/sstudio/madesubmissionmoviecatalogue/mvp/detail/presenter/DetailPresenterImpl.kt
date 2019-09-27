package com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import com.sstudio.madesubmissionmoviecatalogue.FavoriteWidget
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailView
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.movie_wrapper.*

class DetailPresenterImpl(
    private val context: Context,
    private val detailView: DetailView,
    private val favoriteInteractor: FavoriteInteractor
) : DetailPresenter {

    private val disposable = CompositeDisposable()
    private var isShowFavorite = false

    override fun favoriteClick(movieTv: MovieTv) {
        if (isShowFavorite) {
            removeFavorite(movieTv.id)
        } else {
            addFavorite(movieTv)
        }
    }

    override fun loadFavorite(id: Int) {
        disposable.add(
            favoriteInteractor.getFavoriteById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //set favorite button
                    if (it.isNotEmpty()) {
                        isShowFavorite = true
                        detailView.isShowFavorite(R.drawable.ic_favorite_pink_24dp)
                    } else {
                        isShowFavorite = false
                        detailView.isShowFavorite(R.drawable.ic_favorite_white_24dp)
                    }

                }, {
                    detailView.toast("${context.getString(R.string.error_data)} ${it?.message}")
                })
        )
    }

    override fun addFavorite(movieTv: MovieTv) {
        Completable.fromAction {
            favoriteInteractor.insertFavorite(movieTv)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(object :
            CompletableObserver {
            override fun onComplete() {
                detailView.toast("${context.getString(R.string.favorite_added)} ")
                widgetNotifChanged()
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                detailView.toast("${e.message}")
            }

        })
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