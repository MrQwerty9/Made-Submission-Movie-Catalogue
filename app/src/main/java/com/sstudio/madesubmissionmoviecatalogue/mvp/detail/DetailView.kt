package com.sstudio.madesubmissionmoviecatalogue.mvp.detail

import com.sstudio.madesubmissionmoviecatalogue.model.*

interface DetailView {
    fun isShowFavorite(resource: Int)
    fun toast(text: String?)
    fun showAdditionalDetails(detail: Detail, castResponse: CastResponse, videoResponse: VideoResponse, similarResponse: MoviesResponse)
    fun broadcastIntent()
    fun showOverviewEN(string: String)
    fun showTranslate(text: String)
}