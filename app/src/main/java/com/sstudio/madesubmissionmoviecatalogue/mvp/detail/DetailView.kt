package com.sstudio.madesubmissionmoviecatalogue.mvp.detail

import com.sstudio.madesubmissionmoviecatalogue.model.CastResponse
import com.sstudio.madesubmissionmoviecatalogue.model.Detail
import com.sstudio.madesubmissionmoviecatalogue.model.VideoResponse

interface DetailView {
    fun isShowFavorite(resource: Int)
    fun toast(text: String?)
    fun showAdditionalDetails(detail: Detail?, castResponse: CastResponse?, videoResponse: VideoResponse?)
    fun broadcastIntent()
}