package com.sstudio.madesubmissionmoviecatalogue.di.module

import com.sstudio.madesubmissionmoviecatalogue.data.api.MovieDbApi
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDao
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.DetailInteractor
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.DetailInteractorImpl
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvInteractor
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvInteractorImpl
import dagger.Module
import dagger.Provides

@Module
class InteractorModule {
    @Provides
    fun provideFavoriteInteractor(favoriteDao: FavoriteDao): DetailInteractor {
        return DetailInteractorImpl(
            favoriteDao
        )
    }

    @Provides
    fun provideMovieTvInteractor(api : MovieDbApi): MovieTvInteractor {
        return MovieTvInteractorImpl(
            api
        )
    }
}