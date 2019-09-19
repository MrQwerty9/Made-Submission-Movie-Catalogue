package com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter

import android.content.Context
import com.sstudio.madesubmissionmoviecatalogue.R
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailView
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailPresenterImpl(
    private val context: Context,
    private val detailView: DetailView,
    private val detailInteractor: DetailInteractor
) : DetailPresenter {

    private val disposable = CompositeDisposable()

    override fun loadFavorite(id: Int) {
        disposable.add(
            detailInteractor.getFavoriteById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    detailView.isShowFavorite(it.isNotEmpty())
                }, {
                    detailView.toast("${context.getString(R.string.error_data)} ${it?.message}")
                })
        )
    }

    override fun addFavorite(movieTv: MovieTv) {
        Completable.fromAction {
            detailInteractor.insertFavorite(movieTv)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(object :
            CompletableObserver {
            override fun onComplete() {
                detailView.toast("${context.getString(R.string.favorite_added)} ")
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
            detailInteractor.removeFavorite(id)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(object :
            CompletableObserver {
            override fun onComplete() {
                detailView.toast("${context.getString(R.string.favorite_removed)} ")
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
}