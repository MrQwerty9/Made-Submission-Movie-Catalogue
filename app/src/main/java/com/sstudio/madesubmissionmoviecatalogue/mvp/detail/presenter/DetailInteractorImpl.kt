package com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter

import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDao
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import io.reactivex.Flowable

class DetailInteractorImpl(private val favoriteDao: FavoriteDao) :
    DetailInteractor {
    override fun getFavorite(): Flowable<List<MovieTv>> {
        return favoriteDao.getAllList()
    }

    override fun getFavoriteById(id: Int): Flowable<List<MovieTv>> {
        return favoriteDao.getById(id)
    }

    override fun insertFavorite(movieTv: MovieTv) {
        favoriteDao.insert(movieTv)
    }

    override fun removeFavorite(id: Int) {
        favoriteDao.delete(id)
    }
}