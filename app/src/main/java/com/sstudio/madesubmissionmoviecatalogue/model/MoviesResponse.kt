package com.sstudio.madesubmissionmoviecatalogue.model

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("results")
    var movieTv: List<MovieTv>
)
