package com.sstudio.madesubmissionmoviecatalogue.di.component

import com.sstudio.madesubmissionmoviecatalogue.reminder.AlarmReceiver
import com.sstudio.madesubmissionmoviecatalogue.widget.StackRemoteViewsFactory
import com.sstudio.madesubmissionmoviecatalogue.di.AppScope
import com.sstudio.madesubmissionmoviecatalogue.di.module.InteractorModule
import com.sstudio.madesubmissionmoviecatalogue.provider.MovieTvProvider
import dagger.Subcomponent

@AppScope
@Subcomponent(modules = [InteractorModule::class])
interface InteractorComponent {
    fun inject(stackRemoteViewsFactory: StackRemoteViewsFactory)
    fun inject(alarmReceiver: AlarmReceiver)
    fun inject(movieTvProvider: MovieTvProvider)
}