package com.sstudio.madesubmissionmoviecatalogue.mvp.presenter

import android.database.Cursor

interface MovieTvPresenter {
    fun init()
    fun loadFavoriteProvider(): Cursor?
    fun showFavoriteProvider(cursor: Cursor, isMovie: Boolean)
}