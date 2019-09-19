package com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter

import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import io.reactivex.Flowable

interface DetailInteractor {
    fun getFavorite(): Flowable<List<MovieTv>>
    fun getFavoriteById(id: Int): Flowable<List<MovieTv>>
    fun insertFavorite(movieTv: MovieTv)
    fun removeFavorite(id: Int)
}