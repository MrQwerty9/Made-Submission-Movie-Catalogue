package com.sstudio.madesubmissionmoviecatalogue.data.api

import com.sstudio.madesubmissionmoviecatalogue.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbApi {

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<MoviesResponse>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<MoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<MoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<MoviesResponse>

    @GET("discover/movie")
    fun getDiscoverMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("sort_by") sortBy: String,
        @Query("page") page: Int,
        @Query("release_date.gte") dateGte: String,
        @Query("release_date.lte") dateLte: String,
        @Query("vote_count.gte") voteCountGte: Int,
        @Query("with_cast") cast: String,
        @Query("with_genres") genre: String,
        @Query("with_original_language") region: String
    ): Call<MoviesResponse>


    @GET("search/movie")
    fun findMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("query") query: String,
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

    @GET("{movie_tv}/{movie_id}/similar")
    fun getMovieTvSimilar(
        @Path("movie_tv") movieOrTv: String,
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<MoviesResponse>

    @GET("genre/movie/list")
    fun getMovieGenreList(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<Genres>

    @GET("genre/tv/list")
    fun getTvGenreList(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<Genres>
}