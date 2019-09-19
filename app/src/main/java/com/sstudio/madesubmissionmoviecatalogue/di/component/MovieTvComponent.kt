package com.sstudio.madesubmissionmoviecatalogue.di.component

import com.sstudio.madesubmissionmoviecatalogue.di.AppScope
import com.sstudio.madesubmissionmoviecatalogue.di.module.InteractorModule
import com.sstudio.madesubmissionmoviecatalogue.di.module.MovieTvModule
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view.MovieFragment
import com.sstudio.madesubmissionmoviecatalogue.mvp.movie.view.TvShowFragment
import dagger.Subcomponent

@AppScope
@Subcomponent(modules = [MovieTvModule::class, InteractorModule::class])
interface MovieTvComponent {
    fun inject(movieFragment: MovieFragment)
    fun inject(tvShowFragment: TvShowFragment)
}