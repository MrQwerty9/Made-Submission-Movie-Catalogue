package com.sstudio.madesubmissionmoviecatalogue.model

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcelable
import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.sstudio.madesubmissionmoviecatalogue.model.MovieTv.Companion.TABLE_NAME
import kotlinx.android.parcel.Parcelize

@Entity(tableName = TABLE_NAME)
@Parcelize
data class MovieTv(
    @ColumnInfo(name = COLUMN_POSTERPATH)
    @SerializedName("poster_path")
    var posterPath: String = "",

    @ColumnInfo(name = COLUMN_OVERVIEW)
    @SerializedName("overview")
    var overview: String = "",

    @ColumnInfo(name = COLUMN_RELEASEDATE)
    @SerializedName("release_date")
    var releaseDate: String = "",

    @ColumnInfo(name = COLUMN_ID)
    @SerializedName("id")
    @PrimaryKey var id: Int = 0,

    @ColumnInfo(name = COLUMN_TITLE)
    @SerializedName("original_title")
    var title: String = "",

    @ColumnInfo(name = COLUMN_VOTEAVERAGE)
    @SerializedName("vote_average")
    var voteAverage: Double = 0.0,

    @ColumnInfo(name = COLUMN_VOTECOUNT)
    @SerializedName("vote_count")
    var voteCount: Int = 0,

    @ColumnInfo(name = COLUMN_NAME)
    @SerializedName("original_name")
    var name: String = "",

    @ColumnInfo(name = COLUMN_FIRSTAIRDATE)
    @SerializedName("first_air_date")
    var firstAirDate: String = "",

    @ColumnInfo(name = COLUMN_GENRE)
    var genre: String = "",

    @ColumnInfo(name = COLUMN_ISMOVIE)
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

