package com.sstudio.madesubmissionmoviecatalogue

import android.app.Application
import com.sstudio.madesubmissionmoviecatalogue.data.api.ApiModule
import com.sstudio.madesubmissionmoviecatalogue.data.api.NetworkModule
import com.sstudio.madesubmissionmoviecatalogue.di.*
import com.sstudio.madesubmissionmoviecatalogue.di.component.AppComponent
import com.sstudio.madesubmissionmoviecatalogue.di.component.DaggerAppComponent
import com.sstudio.madesubmissionmoviecatalogue.di.component.DetailComponent
import com.sstudio.madesubmissionmoviecatalogue.di.component.MovieTvComponent
import com.sstudio.madesubmissionmoviecatalogue.di.module.DetailModule
import com.sstudio.madesubmissionmoviecatalogue.di.module.InteractorModule
import com.sstudio.madesubmissionmoviecatalogue.di.module.MovieTvModule
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailView
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view.MovieTvView

class App: Application() {
    private lateinit var appComponent : AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = createAppComponent()
    }

    private fun createAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            .apiModule(ApiModule()).build()
    }

    fun createMovieComponent(movieTvView: MovieTvView): MovieTvComponent {
        return appComponent.plus(
            MovieTvModule(
                movieTvView
            ), InteractorModule()
        )
    }

    fun createFavoriteComponent(detailView: DetailView): DetailComponent {
        return appComponent.plus(
            DetailModule(
                detailView
            ), InteractorModule()
        )
    }
}