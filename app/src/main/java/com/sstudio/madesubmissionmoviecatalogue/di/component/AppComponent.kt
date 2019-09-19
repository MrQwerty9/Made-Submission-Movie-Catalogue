package com.sstudio.madesubmissionmoviecatalogue.di.component

import com.sstudio.madesubmissionmoviecatalogue.data.api.ApiModule
import com.sstudio.madesubmissionmoviecatalogue.data.api.NetworkModule
import com.sstudio.madesubmissionmoviecatalogue.data.local.RoomModule
import com.sstudio.madesubmissionmoviecatalogue.di.AppModule
import com.sstudio.madesubmissionmoviecatalogue.di.module.DetailModule
import com.sstudio.madesubmissionmoviecatalogue.di.module.InteractorModule
import com.sstudio.madesubmissionmoviecatalogue.di.module.MovieTvModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ApiModule::class, RoomModule::class, NetworkModule::class])
interface AppComponent {
    fun plus(movieTvModule: MovieTvModule, interactorModule: InteractorModule): MovieTvComponent
    fun plus(detailModule: DetailModule, interactorModule: InteractorModule): DetailComponent
}