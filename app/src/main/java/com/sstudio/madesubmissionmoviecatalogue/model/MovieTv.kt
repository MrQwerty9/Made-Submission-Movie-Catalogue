package com.sstudio.madesubmissionmoviecatalogue.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

@Parcelize
class MovieTv(
    @SerializedName("poster_path")
    var posterPath: String = "",
    @SerializedName("overview")
    var overview: String = "",
    @SerializedName("release_date")
    var releaseDate: String = "",
    @SerializedName("genre_ids")
    var genreIds: List<Int> = ArrayList(),
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("original_title")
    var title: String = "",
    @SerializedName("vote_average")
    var voteAverage: Double = 0.0,
    @SerializedName("vote_count")
    var voteCount: Int = 0,
    @SerializedName("original_name")
    var name: String = "",
    @SerializedName("first_air_date")
    var firstAirDate: String = "",
    var isMovie: Boolean = false
) : Parcelable