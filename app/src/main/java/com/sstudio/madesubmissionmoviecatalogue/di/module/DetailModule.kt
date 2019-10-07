package com.sstudio.madesubmissionmoviecatalogue.di.module

import android.content.Context
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailView
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.DetailPresenter
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.DetailPresenterImpl
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvInteractor
import dagger.Module
import dagger.Provides

@Module
class DetailModule(private val detailView: DetailView) {

    @Provides
    fun provideDetailView(): DetailView {
        return detailView
    }

    @Provides
    fun provideDetailPresenter(context: Context, detailView: DetailView, movieTvInteractor: MovieTvInteractor): DetailPresenter {
        return DetailPresenterImpl(
            context,
            detailView,
            movieTvInteractor
        )
    }
}