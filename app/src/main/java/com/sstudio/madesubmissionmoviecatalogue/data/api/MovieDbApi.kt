package com.sstudio.madesubmissionmoviecatalogue.data.api

import com.sstudio.madesubmissionmoviecatalogue.model.CastResponse
import com.sstudio.madesubmissionmoviecatalogue.model.Detail
import com.sstudio.madesubmissionmoviecatalogue.model.MoviesResponse
import com.sstudio.madesubmissionmoviecatalogue.model.VideoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbApi {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<MoviesResponse>

    @GET("search/movie")
    fun findMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("query") query: String
    ): Call<MoviesResponse>

    @GET("search/tv")
    fun findTv(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("query") query: String
    ): Call<MoviesResponse>

    @GET("tv/popular")
    fun getPopularTv(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<MoviesResponse>

    @GET("discover/movie")
    fun getMovieRelease(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("primary_release_date.gte") dateGte: String,
        @Query("primary_release_date.lte") dateLte: String
    ): Call<MoviesResponse>

    @GET("{movie_tv}/{movie_id}")
    fun getMovieTvDetail(
        @Path("movie_tv") movieTv: String,
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
        ): Call<Detail>

    @GET("{movie_tv}/{movie_id}/credits")
    fun getMovieTvCredits(
        @Path("movie_tv") movieTv: String,
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<CastResponse>

    @GET("{movie_tv}/{movie_id}/videos")
    fun getMovieTvVideo(
        @Path("movie_tv") movieTv: String,
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<VideoResponse>
}