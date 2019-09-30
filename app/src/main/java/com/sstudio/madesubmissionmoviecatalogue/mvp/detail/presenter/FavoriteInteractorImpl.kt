package com.sstudio.madesubmissionmoviecatalogue.mvp.detail.presenter

import android.database.Cursor
import com.sstudio.madesubmissionmoviecatalogue.data.local.FavoriteDao
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv
import io.reactivex.Flowable

class FavoriteInteractorImpl(private val favoriteDao: FavoriteDao) :
    FavoriteInteractor {
    override fun getFavorite(): Flowable<List<MovieTv>> {
        return favoriteDao.getFavorite()
    }

    override fun getFavoriteSync(): List<MovieTv> {
        return favoriteDao.getFavoriteSync()
    }

    override fun getFavoriteById(id: Int): Flowable<List<MovieTv>> {
        return favoriteDao.getById(id)
    }

    override fun getFavoriteCursor(): Cursor {
        return favoriteDao.getFavoriteCursor()
    }

    override fun getFavByIdCursor(id: Int): Cursor {
        return favoriteDao.getFavByIdCursor(id)
    }

    override fun insertFavorite(movieTv: MovieTv) {
        favoriteDao.insert(movieTv)
    }

    override fun removeFavorite(id: Int) {
        favoriteDao.delete(id)
    }
}