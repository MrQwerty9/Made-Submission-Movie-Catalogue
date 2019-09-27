package com.sstudio.madesubmissionmoviecatalogue.mvp.detail

interface DetailView {
    fun isShowFavorite(resource: Int)
    fun toast(text: String?)
}