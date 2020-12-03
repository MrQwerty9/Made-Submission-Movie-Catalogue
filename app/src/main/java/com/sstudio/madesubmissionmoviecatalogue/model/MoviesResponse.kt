package com.sstudio.madesubmissionmoviecatalogue.model

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("total_pages")
    var totalPages: Int = 0,
    @SerializedName("results")
    var movieTv: List<MovieTv>
)
