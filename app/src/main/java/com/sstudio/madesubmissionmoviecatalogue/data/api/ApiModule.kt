package com.sstudio.madesubmissionmoviecatalogue.data.api

import com.sstudio.madesubmissionmoviecatalogue.BuildConfig
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule{
    @Provides
    @Singleton
    @Named("baseUrl")
    fun provideBaeUrl(): String{
        return BuildConfig.BASE_URL
    }

    @Provides
    @Singleton
    fun provideMovieDbApi(retrofit: Retrofit): MovieDbApi {
        return retrofit.create(MovieDbApi::class.java)
    }
}