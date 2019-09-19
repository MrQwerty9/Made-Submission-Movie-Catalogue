package com.sstudio.madesubmissionmoviecatalogue.di.component

import com.sstudio.madesubmissionmoviecatalogue.di.AppScope
import com.sstudio.madesubmissionmoviecatalogue.di.module.DetailModule
import com.sstudio.madesubmissionmoviecatalogue.di.module.InteractorModule
import com.sstudio.madesubmissionmoviecatalogue.mvp.detail.DetailActivity
import dagger.Subcomponent

@AppScope
@Subcomponent(modules = [DetailModule::class, InteractorModule::class])
interface DetailComponent {
    fun inject(detailActivity: DetailActivity)
}