package com.sstudio.madesubmissionmoviecatalogue.model

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("results")
    var video: List<Video>
)