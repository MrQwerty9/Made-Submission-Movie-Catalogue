package com.sstudio.madesubmissionmoviecatalogue

import androidx.multidex.MultiDexApplication
import com.sstudio.madesubmissionmoviecatalogue.data.api.ApiModule
import com.sstudio.madesubmissionmoviecatalogue.data.api.NetworkModule
import com.sstudio.madesubmissionmoviecatalogue.data.local.RoomModule
import com.sstudio.madesubmissionmoviecatalogue.di.*
import com.sstudio.madesubmissionmoviecatalogue.di.component.*
import com.sstudio.madesubmissionmoviecatalogue.di.module.DetailModule
import com.sstudio.madesubmissionmoviecatalogue.di.module.InteractorModule
import com.sstudio.madesubmissionmoviecatalogue.di.module.MovieTvModule
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailView
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view.MovieTvView

class App: MultiDexApplication() {
    private lateinit var appComponent : AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = createAppComponent()
    }

    private fun createAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            .apiModule(ApiModule())
            .roomModule(RoomModule())
            .build()
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

    fun createInteractorComponent(): InteractorComponent{
        return appComponent.plus(InteractorModule())
    }

}