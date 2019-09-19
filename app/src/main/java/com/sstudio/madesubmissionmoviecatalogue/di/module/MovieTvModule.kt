package com.sstudio.madesubmissionmoviecatalogue.di.module

import android.content.Context
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.DetailInteractor
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvPresenter
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvPresenterImpl
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvInteractor
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view.MovieTvView
import dagger.Module
import dagger.Provides

@Module
class MovieTvModule(private val movieTvView: MovieTvView) {

    @Provides
    fun provideMovieTvView(): MovieTvView {
        return movieTvView
    }

    @Provides
    fun provideMovieTvPresenter(context: Context, movieTvView: MovieTvView, movieTvInteractor: MovieTvInteractor,
                                detailInteractor: DetailInteractor
    ): MovieTvPresenter {
        return MovieTvPresenterImpl(
            context,
            movieTvView,
            movieTvInteractor,
            detailInteractor
        )
    }
}