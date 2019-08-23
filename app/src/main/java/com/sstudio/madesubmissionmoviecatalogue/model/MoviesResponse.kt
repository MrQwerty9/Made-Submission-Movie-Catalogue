package com.sstudio.madesubmissionmoviecatalogue.model

import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv

data class MoviesResponse (
    @SerializedName("results")
    var movieTv: List<MovieTv>
)
