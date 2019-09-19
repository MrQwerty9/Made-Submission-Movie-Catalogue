package com.sstudio.madesubmissionmoviecatalogue.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favorite")
@Parcelize
data class MovieTv(
    @ColumnInfo(name = "posterPath")
    @SerializedName("poster_path")
    var posterPath: String = "",
    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    var overview: String = "",
    @ColumnInfo(name = "releaseDate")
    @SerializedName("release_date")
    var releaseDate: String = "",
    @SerializedName("id")
    @PrimaryKey var id: Int = 0,
    @ColumnInfo(name = "title")
    @SerializedName("original_title")
    var title: String = "",
    @ColumnInfo(name = "voteAverage")
    @SerializedName("vote_average")
    var voteAverage: Double = 0.0,
    @ColumnInfo(name = "voteCount")
    @SerializedName("vote_count")
    var voteCount: Int = 0,
    @ColumnInfo(name = "name")
    @SerializedName("original_name")
    var name: String = "",
    @ColumnInfo(name = "firstAirDate")
    @SerializedName("first_air_date")
    var firstAirDate: String = "",
    @ColumnInfo(name = "genre")
    var genre: String = "",
    @ColumnInfo(name = "isMovie")
    var isMovie: Int = 0
) : Parcelable
