package com.sstudio.madesubmissionmoviecatalogue.model

import com.google.gson.annotations.SerializedName

data class Detail(
    @SerializedName("backdrop_path")
    val backdropPath: String? = "",
    val genres: List<Genres.Genre>,
    val overview: String = ""
)