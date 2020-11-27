package com.sstudio.madesubmissionmoviecatalogue.di.module

import android.content.Context
import com.sstudio.madesubmissionmoviecatalogue.data.api.MovieDbApi
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDao
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.FavoriteInteractor
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter.FavoriteInteractorImpl
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvInteractor
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.presenter.MovieTvInteractorImpl
import dagger.Module
import dagger.Provides

@Module
class InteractorModule {
    @Provides
    fun provideFavoriteInteractor(favoriteDao: FavoriteDao): FavoriteInteractor {
        return FavoriteInteractorImpl(
            favoriteDao
        )
    }

    @Provides
    fun provideMovieTvInteractor(api : MovieDbApi, context: Context): MovieTvInteractor {
        return MovieTvInteractorImpl(
            api, context
        )
    }
}