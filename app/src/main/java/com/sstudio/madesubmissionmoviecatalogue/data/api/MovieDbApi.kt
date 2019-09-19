package com.sstudio.madesubmissionmoviecatalogue.data.api

import com.sstudio.madesubmissionmoviecatalogue.model.MoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDbApi {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<MoviesResponse>

    @GET("tv/popular")
    fun getPopularTv(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<MoviesResponse>

}