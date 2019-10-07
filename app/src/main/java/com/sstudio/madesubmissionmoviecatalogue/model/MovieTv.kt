package com.sstudio.madesubmissionmoviecatalogue.model

import android.content.ContentValues
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.TABLE_NAME
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieTv(
    var posterPath: String = "",
    var overview: String = "",
    var releaseDate: String = "",
    var id: Int = 0,
    var title: String = "",
    var voteAverage: Double = 0.0,
    var voteCount: Int = 0,
    var name: String = "",
    var firstAirDate: String = "",
    var genre: String = "",
    var isMovie: Int = 0
) : Parcelable{

    companion object {
        const val TABLE_NAME = "favorite"
        const val COLUMN_ID = "id"
        const val COLUMN_POSTERPATH = "posterPath"
        const val COLUMN_OVERVIEW = "overview"
        const val COLUMN_RELEASEDATE = "releaseDate"
        const val COLUMN_TITLE = "title"
        const val COLUMN_VOTEAVERAGE = "voteAverage"
        const val COLUMN_VOTECOUNT = "voteCount"
        const val COLUMN_NAME = "name"
        const val COLUMN_FIRSTAIRDATE = "firstAirDate"
        const val COLUMN_GENRE = "genre"
        const val COLUMN_ISMOVIE = "isMovie"


        fun fromContentValues(values: ContentValues?): MovieTv {
            val menu = MovieTv()
            if (values != null) {
                if (values.containsKey(COLUMN_ID)) {
                    menu.id = values.getAsInteger(COLUMN_ID)
                }

                if (values.containsKey(COLUMN_NAME)) {
                    menu.name = values.getAsString(COLUMN_NAME)
                }
                menu.posterPath = values.getAsString(COLUMN_POSTERPATH)
                menu.overview = values.getAsString(COLUMN_OVERVIEW)
                menu.releaseDate = values.getAsString(COLUMN_RELEASEDATE)
                menu.title = values.getAsString(COLUMN_TITLE)
                menu.voteAverage = values.getAsDouble(COLUMN_VOTEAVERAGE)
                menu.voteCount = values.getAsInteger(COLUMN_VOTECOUNT)
                menu.firstAirDate = values.getAsString(COLUMN_FIRSTAIRDATE)
                menu.genre = values.getAsString(COLUMN_GENRE)
                menu.isMovie = values.getAsInteger(COLUMN_ISMOVIE)
            }
            return menu
        }
    }
}

