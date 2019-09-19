package com.sstudio.madesubmissionmoviecatalogue.mvp.detail

interface DetailView {
    fun isShowFavorite(isFav: Boolean)
    fun toast(text: String?)
}